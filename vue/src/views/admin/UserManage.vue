<template>
  <div class="user-manage">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="关键词"><el-input v-model="searchForm.keyword" placeholder="账号/昵称/手机号" clearable /></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch"><el-icon><Search /></el-icon>搜索</el-button>
          <el-button @click="handleReset"><el-icon><Refresh /></el-icon>重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="table-card">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="账号" width="120" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="role" label="角色" width="100"><template #default="{ row }"><el-tag :type="getRoleType(row.role)">{{ getRoleLabel(row.role) }}</el-tag></template></el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="creditScore" label="信用分" width="80" />
        <el-table-column prop="status" label="状态" width="80"><template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '正常' : '禁用' }}</el-tag></template></el-table-column>
        <el-table-column label="操作" fixed="right" width="150"><template #default="{ row }"><el-button v-if="row.role !== 'admin'" :type="row.status === 1 ? 'danger' : 'success'" size="small" @click="handleToggleStatus(row)">{{ row.status === 1 ? '禁用' : '启用' }}</el-button></template></el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination :current-page="pagination.pageNum" :page-size="pagination.pageSize" :page-sizes="[10, 20, 50]" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handlePageChange" />
      </div>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getUserList, updateUserStatus } from '@/api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'
const loading = ref(false)
const tableData = ref([])
const searchForm = reactive({ keyword: '' })
const pagination = reactive({ pageNum: 1, pageSize: 10, total: 0 })
const roleMap = { admin: { label: '管理员', type: 'danger' }, staff: { label: '库管员', type: 'warning' }, renter: { label: '租客', type: 'primary' } }
const getRoleLabel = (role) => roleMap[role]?.label || role
const getRoleType = (role) => roleMap[role]?.type || 'info'
const loadData = async () => {
  loading.value = true
  try { const res = await getUserList({ pageNum: pagination.pageNum, pageSize: pagination.pageSize, keyword: searchForm.keyword }); tableData.value = res.data.records; pagination.total = res.data.total }
  catch (error) { console.error(error) } finally { loading.value = false }
}
const handleSearch = () => { pagination.pageNum = 1; loadData() }
const handleReset = () => { searchForm.keyword = ''; pagination.pageNum = 1; loadData() }
const handleSizeChange = (size) => { pagination.pageSize = size; pagination.pageNum = 1; loadData() }
const handlePageChange = (page) => { pagination.pageNum = page; loadData() }
const handleToggleStatus = async (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  const action = newStatus === 0 ? '禁用' : '启用'
  try { await ElMessageBox.confirm(`确定要${action}用户"${row.nickname}"吗？`, '提示', { type: 'warning' }); await updateUserStatus(row.id, newStatus); ElMessage.success(`${action}成功`); loadData() }
  catch (error) { if (error !== 'cancel') console.error(error) }
}
onMounted(() => { loadData() })
</script>
<style scoped>
.search-card { margin-bottom: 20px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
