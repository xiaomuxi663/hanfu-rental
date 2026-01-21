<template>
  <div class="notice-list">
    <!-- 轮播图 -->
    <el-carousel v-if="bannerList.length > 0" height="300px" class="banner-carousel">
      <el-carousel-item v-for="banner in bannerList" :key="banner.id">
        <div class="banner-item">
          <img v-if="banner.content" :src="banner.content" :alt="banner.title" />
          <div class="banner-title">{{ banner.title }}</div>
        </div>
      </el-carousel-item>
    </el-carousel>

    <!-- 公告列表 -->
    <el-card class="notice-card">
      <template #header>
        <div class="card-header">
          <el-icon><Bell /></el-icon>
          <span>系统公告</span>
        </div>
      </template>
      
      <div v-if="noticeList.length > 0" class="notice-items">
        <div 
          v-for="notice in noticeList" 
          :key="notice.id" 
          class="notice-item"
          @click="showNoticeDetail(notice)"
        >
          <el-tag type="danger" size="small">公告</el-tag>
          <span class="notice-title">{{ notice.title }}</span>
          <span class="notice-time">{{ formatTime(notice.createTime) }}</span>
        </div>
      </div>
      <el-empty v-else description="暂无公告" />
    </el-card>

    <!-- 公告详情对话框 -->
    <el-dialog v-model="detailVisible" :title="currentNotice.title" width="600px">
      <div class="notice-content" v-html="currentNotice.content"></div>
      <div class="notice-footer">
        发布时间：{{ currentNotice.createTime }}
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Bell } from '@element-plus/icons-vue'
import request from '@/utils/request'

const bannerList = ref([])
const noticeList = ref([])
const detailVisible = ref(false)
const currentNotice = ref({})

// 获取轮播图
const fetchBanners = async () => {
  try {
    const res = await request.get('/api/notice/banners')
    bannerList.value = res.data || []
  } catch (error) {
    console.error('获取轮播图失败', error)
  }
}

// 获取公告列表
const fetchNotices = async () => {
  try {
    const res = await request.get('/api/notice/list')
    noticeList.value = res.data || []
  } catch (error) {
    console.error('获取公告列表失败', error)
  }
}

// 显示公告详情
const showNoticeDetail = (notice) => {
  currentNotice.value = notice
  detailVisible.value = true
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  return time.substring(0, 10)
}

onMounted(() => {
  fetchBanners()
  fetchNotices()
})
</script>

<style scoped>
.notice-list {
  padding: 20px;
}

.banner-carousel {
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
}

.banner-item {
  width: 100%;
  height: 100%;
  position: relative;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.banner-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-title {
  position: absolute;
  bottom: 20px;
  left: 20px;
  color: #fff;
  font-size: 24px;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.notice-card .card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: bold;
}

.notice-items {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.notice-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.notice-item:hover {
  background-color: #f5f7fa;
}

.notice-title {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.notice-time {
  color: #909399;
  font-size: 12px;
}

.notice-content {
  line-height: 1.8;
  min-height: 100px;
}

.notice-footer {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #eee;
  color: #909399;
  font-size: 12px;
  text-align: right;
}
</style>
