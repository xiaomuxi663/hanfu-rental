<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6"><el-card shadow="hover"><template #header>用户总数</template><div class="stat-value">{{ stats.userCount }}</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><template #header>订单总数</template><div class="stat-value">{{ stats.orderCount }}</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><template #header>汉服款式</template><div class="stat-value">{{ stats.hanfuCount }}</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><template #header>今日营收</template><div class="stat-value">￥{{ stats.todayRevenue }}</div></el-card></el-col>
    </el-row>
    <el-card style="margin-top: 20px;">
      <template #header>系统信息</template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="系统名称">汉服租赁系统</el-descriptions-item>
        <el-descriptions-item label="版本">1.0.0</el-descriptions-item>
        <el-descriptions-item label="前端技术">Vue 3 + Element Plus</el-descriptions-item>
        <el-descriptions-item label="后端技术">SpringBoot 3.x + MyBatis-Plus</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>
<script setup>
import { reactive } from 'vue'
const stats = reactive({ userCount: 0, orderCount: 0, hanfuCount: 0, todayRevenue: '0.00' })
</script>
<style scoped>
.stat-value { font-size: 32px; font-weight: bold; color: #409EFF; }
</style>
