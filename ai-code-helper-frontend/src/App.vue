<script setup>
import { ref, onMounted, nextTick } from 'vue'
import axios from 'axios'
import { marked } from 'marked'

// 聊天消息结构：{ role: 'user' | 'ai', content: string }
const messages = ref([])
const input = ref('')
const memoryId = ref('')
const chatContainer = ref(null)
const isLoading = ref(false)
let eventSource = null

// 只生成数字型 memoryId
function generateMemoryId() {
  // 生成 1~10亿以内的正整数
  return Math.floor(Math.random() * 1_000_000_000);
}

onMounted(() => {
  memoryId.value = generateMemoryId()
})

// 发送消息（后续集成 SSE）
async function sendMessage() {
  const msg = input.value.trim()
  if (!msg || isLoading.value) return
  messages.value.push({ role: 'user', content: msg })
  input.value = ''
  await nextTick()
  scrollToBottom()
  startSSE(msg)
}

function startSSE(userMsg) {
  if (eventSource) {
    eventSource.close()
    eventSource = null
  }
  isLoading.value = true
  let aiMsg = { role: 'ai', content: '' }
  messages.value.push(aiMsg)
  const url = `http://localhost:8081/api/ai/chat?memoryId=${encodeURIComponent(memoryId.value)}&message=${encodeURIComponent(userMsg)}`
  eventSource = new window.EventSource(url)
  eventSource.onmessage = (e) => {
    if (e.data) {
      aiMsg.content += e.data
      scrollToBottom()
    }
  }
  eventSource.onerror = () => {
    isLoading.value = false
    eventSource && eventSource.close()
    eventSource = null
  }
  eventSource.onopen = () => {}
  eventSource.addEventListener('end', () => {
    isLoading.value = false
    eventSource && eventSource.close()
    eventSource = null
  })
}

function scrollToBottom() {
  if (chatContainer.value) {
    chatContainer.value.scrollTop = chatContainer.value.scrollHeight
  }
}

function renderMarkdown(md) {
  return marked.parse(md)
}
</script>

<template>
  <div class="chat-app">
    <h1 class="title">AI 编程小助手</h1>
    <div class="chat-container" ref="chatContainer">
      <div v-for="(msg, idx) in messages" :key="idx" :class="['chat-message', msg.role]">
        <div class="bubble">
          <template v-if="msg.role === 'ai'">
            <div v-html="renderMarkdown(msg.content)"></div>
          </template>
          <template v-else>
            {{ msg.content }}
          </template>
        </div>
      </div>
      <div v-if="isLoading" class="loading">AI 正在回复...</div>
    </div>
    <div class="input-area">
      <input v-model="input" @keyup.enter="sendMessage" :disabled="isLoading" placeholder="请输入你的编程或面试问题..." />
      <button @click="sendMessage" :disabled="isLoading">发送</button>
    </div>
  </div>
</template>

<style scoped>
/* 优化页面宽度和居中展示 */
.chat-app {
  max-width: 900px;
  min-width: 340px;
  margin: 0 auto;
  background: var(--color-background-soft);
  border-radius: 12px;
  box-shadow: 0 2px 16px rgba(0,0,0,0.08);
  padding: 24px 0 0 0;
  display: flex;
  flex-direction: column;
  height: 80vh;
  position: relative;
  top: 50%;
  transform: translateY(8%);
}

body, #app {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-background);
}

.title {
  text-align: center;
  margin-bottom: 12px;
  font-size: 2rem;
  color: var(--color-heading);
}
.chat-container {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background: var(--color-background);
  border-bottom: 1px solid var(--color-border);
}
.chat-message {
  display: flex;
  margin-bottom: 12px;
}
.chat-message.user {
  justify-content: flex-end;
}
.chat-message.ai {
  justify-content: flex-start;
}
.bubble {
  max-width: 70%;
  padding: 10px 16px;
  border-radius: 18px;
  font-size: 1rem;
  line-height: 1.6;
  background: var(--color-background-mute);
  color: var(--color-text);
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
  word-break: break-word;
}
.chat-message.user .bubble {
  background: #4f9cff;
  color: #fff;
  border-bottom-right-radius: 6px;
  border-bottom-left-radius: 18px;
}
.chat-message.ai .bubble {
  background: #f2f2f2;
  color: #222;
  border-bottom-left-radius: 6px;
  border-bottom-right-radius: 18px;
}
.input-area {
  display: flex;
  padding: 16px;
  border-top: 1px solid var(--color-border);
  background: var(--color-background);
}
.input-area input {
  flex: 1;
  padding: 10px 14px;
  border: 1px solid var(--color-border);
  border-radius: 6px;
  font-size: 1rem;
  outline: none;
  margin-right: 8px;
}
.input-area button {
  padding: 0 20px;
  border: none;
  background: #4f9cff;
  color: #fff;
  border-radius: 6px;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s;
}
.input-area button:hover {
  background: #357ae8;
}
.loading {
  text-align: left;
  color: #888;
  font-size: 0.95rem;
  margin: 8px 0 0 8px;
}
</style>
