<template>
  <div class="home">
    <!-- 轮播图 -->
    <div class="banner-section">
      <el-carousel v-if="bannerList.length > 0" height="400px">
        <el-carousel-item v-for="banner in bannerList" :key="banner.id">
          <div class="banner-item" :style="getBannerStyle(banner)">
            <div class="banner-overlay">
              <h2>{{ banner.title }}</h2>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
      <div v-else class="default-banner">
        <h1>汉服租赁系统</h1>
        <p>传承华夏文明，体验汉服之美</p>
      </div>
    </div>

    <div class="main-content">
      <!-- 公告区域 -->
      <el-card class="notice-section" v-if="noticeList.length > 0">
        <template #header>
          <div class="section-header">
            <span><el-icon><Bell /></el-icon> 最新公告</span>
            <el-button type="primary" link @click="$router.push('/notice')">查看更多</el-button>
          </div>
        </template>
        <div class="notice-items">
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
      </el-card>

      <!-- 汉服展示区域占位 -->
      <el-card class="hanfu-section">
        <template #header>
          <div class="section-header">
            <span>热门汉服</span>
          </div>
        </template>
        <el-empty description="汉服展示区域（待开发）">
          <el-button type="primary" @click="$router.push('/login')">去登录</el-button>
        </el-empty>
      </el-card>
    </div>

    <!-- 公告详情对话框 -->
    <el-dialog v-model="detailVisible" :title="currentNotice.title" width="600px">
      <div class="notice-content">{{ currentNotice.content }}</div>
      <div class="notice-footer">
        发布时间：{{ formatTime(currentNotice.createTime) || '暂无' }}
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

// 获取轮播图样式（背景图）
const getBannerStyle = (banner) => {
  if (banner.content) {
    return {
      backgroundImage: `url(${banner.content})`,
      backgroundSize: 'cover',
      backgroundPosition: 'center'
    }
  }
  return {}
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
.home {
  min-height: calc(100vh - 140px);
}

.banner-section {
  margin-bottom: 20px;
}

.banner-item {
  width: 100%;
  height: 100%;
  position: relative;
  background: linear-gradient(135deg, #c41d7f 0%, #722ed1 100%);
}

.banner-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30px;
  background: linear-gradient(transparent, rgba(0,0,0,0.7));
  color: #fff;
}

.banner-overlay h2 {
  margin: 0;
  font-size: 28px;
}

.default-banner {
  background: linear-gradient(135deg, #c41d7f 0%, #722ed1 100%);
  color: #fff;
  text-align: center;
  padding: 120px 20px;
}

.default-banner h1 {
  font-size: 48px;
  margin-bottom: 16px;
}

.default-banner p {
  font-size: 20px;
  opacity: 0.9;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 40px;
}

.notice-section {
  margin-bottom: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
}

.section-header span {
  display: flex;
  align-items: center;
  gap: 8px;
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

.hanfu-section {
  margin-bottom: 20px;
}

.notice-content {
  line-height: 1.8;
  min-height: 100px;
  white-space: pre-wrap;
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
