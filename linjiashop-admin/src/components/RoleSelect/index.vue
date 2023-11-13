<template>
  <el-select
    v-model="roleValue"
    style="width: 100%"
    :size="size"
    :placeholder="placeholder"
    @change="change"
  >
    <el-option
      v-for="item in roleList"
      :key="item.id"
      :label="item.name"
      :value="item.id"
    />
  </el-select>
</template>

<script>
import { getList } from '@/api/system/role'

export default {
  name: 'RoleSelect',
  props: {
    value: {
      type: [String, Number],
      default: ''
    },
    size: {
      type: String,
      default: 'mini'
    },
    placeholder: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      roleList: [],
      roleValue: ''
    }
  },
  watch: {
    value() {
      this.roleValue = this.value
    }
  },
  created() {
    this.getRoleList()
  },
  methods: {
    getRoleList() {
      // 从后台获取字典列表
      getList({}).then(response => {
        console.log('get role list ',this.roleList)
        this.roleList = response.data
      })
    },
    change(value) {
      console.log('value  ',value);
      this.$emit('input', value)
    }
  }
}
</script>
