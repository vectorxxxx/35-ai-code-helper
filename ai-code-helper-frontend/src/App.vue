<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { marked } from 'marked'

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
      messages.value = [...messages.value] // 强制触发响应式刷新
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

function renderMarkdown(md) {
  return marked.parse(md)
}

function scrollToBottom() {
  if (chatListRef.value) {
    chatListRef.value.scrollTop = chatListRef.value.scrollHeight
  }
}
</script>

<template>
  <div class="main-bg">
    <div class="center-card">
      <h1 class="main-title">AI 编程小助手</h1>
      <div class="sub-title">帮助您解答编程学习和求职面试相关问题</div>
      <template v-if="messages.length === 0">
        <div class="robot">🤖</div>
        <div class="welcome-title">欢迎使用 AI 编程小助手</div>
        <ul class="desc-list">
          <li>解答编程技术问题</li>
          <li>提供代码示例和解释</li>
          <li>协助求职面试准备</li>
          <li>分享编程学习建议</li>
        </ul>
        <div class="desc-tip">请随时向我提问吧！</div>
      </template>
    </div>
    <div class="chat-list" ref="chatListRef" v-show="messages.length > 0">
      <div v-for="(msg, idx) in messages" :key="idx" :class="['chat-msg', msg.role]">
        <template v-if="msg.role === 'ai'">
          <div v-if="msg.streaming" class="ai-streaming-preview"><pre>{{ msg.content }}</pre></div>
          <div v-else v-html="renderMarkdown(msg.content)"></div>
        </template>
        <template v-else>
          {{ msg.content }}
        </template>
      </div>
    </div>
    <div class="input-bar">
      <input v-model="input" @keyup.enter="sendMessage" :disabled="isLoading" placeholder="请输入你的编程或面试问题..." />
      <button @click="sendMessage" :disabled="isLoading">发送</button>
    </div>
  </div>
</template>
