import { marked } from 'marked'
export function fixMarkdown(md) {
  // 自动为 #、##、### 等后无空格的标题补空格
  return md.replace(/^(#{1,6})([^\s#])/gm, '$1 $2')
}
export function renderMarkdown(md) {
  return marked.parse(fixMarkdown(md))
} 