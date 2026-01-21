<template>
  <div class="config-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>系统配置管理</span>
          <el-button type="primary" @click="handleAdd">新增配置</el-button>
        </div>
      </template>
      
      <el-table :data="configList" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="paramKey" label="参数键" width="200" />
        <el-table-column prop="paramValue" label="参数值" />
        <el-table-column prop="remark" label="备注" width="200" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="500px"
      @close="resetForm"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="参数键" prop="paramKey">
          <el-input v-model="form.paramKey" :disabled="isEdit" placeholder="请输入参数键" />
        </el-form-item>
        <el-form-item label="参数值" prop="paramValue">
          <el-input 
            v-model="form.paramValue" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入参数值" 
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const configList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增配置')
const isEdit = ref(false)
const formRef = ref(null)

const form = ref({
  id: null,
  paramKey: '',
  paramValue: '',
  remark: ''
})

const rules = {
  paramKey: [{ required: true, message: '请输入参数键', trigger: 'blur' }],
  paramValue: [{ required: true, message: '请输入参数值', trigger: 'blur' }]
}

// 获取配置列表
const fetchConfigList = async () => {
  try {
    const res = await request.get('/api/admin/config/list')
    configList.value = res.data || []
  } catch (error) {
    console.error('获取配置列表失败', error)
  }
}

// 新增
const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增配置'
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑配置'
  form.value = { ...row }
  dialogVisible.value = true
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该配置吗？', '提示', {
      type: 'warning'
    })
    await request.delete(`/api/admin/config/delete/${row.id}`)
    ElMessage.success('删除成功')
    fetchConfigList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
    }
  }
}

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate()
    
    if (isEdit.value) {
      await request.put('/api/admin/config/update', form.value)
      ElMessage.success('更新成功')
    } else {
      await request.post('/api/admin/config/add', form.value)
      ElMessage.success('添加成功')
    }
    
    dialogVisible.value = false
    fetchConfigList()
  } catch (error) {
    console.error('提交失败', error)
  }
}

// 重置表单
const resetForm = () => {
  form.value = {
    id: null,
    paramKey: '',
    paramValue: '',
    remark: ''
  }
  formRef.value?.resetFields()
}

onMounted(() => {
  fetchConfigList()
})
</script>

<style scoped>
.config-manage {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
