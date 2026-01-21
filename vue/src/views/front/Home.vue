<template>
  <div class="home">
    <div class="banner">
      <h1>汉服租赁系统</h1>
      <p>传承华夏文明，体验汉服之美</p>
    </div>
    <div class="content">
      <el-empty description="汉服展示区域（待开发）">
        <el-button type="primary" @click="$router.push('/login')">去登录</el-button>
      </el-empty>
    </div>
  </div>
</template>
<script setup></script>
<style scoped>
.home { min-height: calc(100vh - 140px); }
.banner { background: linear-gradient(135deg, #c41d7f 0%, #722ed1 100%); color: #fff; text-align: center; padding: 80px 20px; }
.banner h1 { font-size: 42px; margin-bottom: 16px; }
.banner p { font-size: 18px; opacity: 0.9; }
.content { max-width: 1200px; margin: 0 auto; padding: 40px 20px; }
</style>
