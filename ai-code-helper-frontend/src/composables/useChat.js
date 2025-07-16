import { ref, onMounted, nextTick } from 'vue'
import { renderMarkdown } from '../utils/markdown'

export function useChat() {
  const input = ref('')
  const isLoading = ref(false)
  const messages = ref([])
  const memoryId = ref(0)
  const chatListRef = ref(null)
  let eventSource = null

  function generateMemoryId() {
    return Math.floor(Math.random() * 1_000_000_000)
  }

  onMounted(() => {
    memoryId.value = generateMemoryId()
  })

  function sendMessage() {
    const msg = input.value.trim()
    if (!msg || isLoading.value) return
    messages.value.push({ role: 'user', content: msg })
    input.value = ''
    isLoading.value = true
    nextTick(() => scrollToBottom())
    startSSE(msg)
  }

  function startSSE(userMsg) {
    if (eventSource) {
      eventSource.close()
      eventSource = null
    }
    const aiMsg = { role: 'ai', content: '', streaming: true }
    messages.value.push(aiMsg)
    const url = `http://localhost:8081/api/ai/chat?memoryId=${memoryId.value}&message=${encodeURIComponent(userMsg)}`
    eventSource = new window.EventSource(url)
    eventSource.addEventListener('message', async (e) => {
      if (e.data) {
        aiMsg.content += e.data
        messages.value = [...messages.value]
        await nextTick()
        scrollToBottom()
      }
    })
    eventSource.onerror = () => {
      isLoading.value = false
      eventSource && eventSource.close()
      eventSource = null
      aiMsg.streaming = false
    }
    eventSource.onopen = () => {}
    eventSource.addEventListener('end', () => {
      isLoading.value = false
      eventSource && eventSource.close()
      eventSource = null
      aiMsg.streaming = false
    })
  }

  function scrollToBottom() {
    if (chatListRef.value) {
      chatListRef.value.scrollTop = chatListRef.value.scrollHeight
    }
  }

  return {
    input,
    isLoading,
    messages,
    memoryId,
    chatListRef,
    sendMessage,
    renderMarkdown,
  }
} 