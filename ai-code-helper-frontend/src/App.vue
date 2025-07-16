<script setup>
import { useChat } from './composables/useChat'
const { input, isLoading, messages, chatListRef, sendMessage, renderMarkdown } = useChat()
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
        <span class="chat-avatar" v-if="msg.role === 'ai'">🤖</span>
        <span class="chat-avatar" v-else>我</span>
        <template v-if="msg.role === 'ai'">
          <div v-if="msg.streaming" class="ai-streaming-preview"><pre>{{ msg.content }}</pre></div>
          <div v-else v-html="renderMarkdown(msg.content)"></div>
        </template>
        <template v-else>
          <div>{{ msg.content }}</div>
        </template>
      </div>
    </div>
    <div class="input-bar">
      <input v-model="input" @keyup.enter="sendMessage" :disabled="isLoading" placeholder="请输入你的编程或面试问题..." />
      <button @click="sendMessage" :disabled="isLoading">发送</button>
    </div>
  </div>
</template>
