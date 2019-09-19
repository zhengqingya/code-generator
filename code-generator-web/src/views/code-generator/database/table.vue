<template>
  <div class="app-container">
    <cus-wraper>
      <cus-filter-wraper>
        <el-input v-model="listQuery.tableName" placeholder="表名称" class="filter-item" style="width:200px"></el-input>
        <el-button class="filter-item" @click="getList" type="primary" icon="el-icon-search">查询</el-button>
      </cus-filter-wraper>
      <div class="table-container">
        <el-table v-loading="listLoading" :data="list" size="mini" element-loading-text="Loading" fit border highlight-current-row>
          <el-table-column align="center" label="ID" width="80" type="index"></el-table-column>
          <el-table-column width="250" label="表名" header-align="center" align="center" prop="tableName"></el-table-column>
          <el-table-column label="注解" header-align="center" align="center" prop="comments"></el-table-column>
          <el-table-column align="center" v-if="this.global_checkBtnPermission(['column'])" :label="$t('table.actions')">
            <template slot-scope="scope">
              <router-link :to="{path:'/codeGenerator/column',
                  query:{
                    tableName:scope.row.tableName,
                    dataBaseId:listQuery.id
                  }}">
                <el-button v-has="'column'" size="mini" type="primary" icon="el-icon-plus" plain> {{ $t('table.edit') }} </el-button>
              </router-link>
            </template>
          </el-table-column>
        </el-table>
<!--        <cus-pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList"/>-->
      </div>
    </cus-wraper>
  </div>
</template>

<script>
  import { fetchTableList } from '@/api/code-generator/database'

  export default {
    name: 'tableInfo',
    data() {
      return {
        list: [],
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 10,
          tableName: undefined,
          id: this.$route.query.dataBaseId
        }
      }
    },
    created() {
      this.getList()
    },
    methods: {
      getList() {
        this.listLoading = true
        fetchTableList(this.listQuery).then(response => {
          this.list = response.data
          this.listLoading = false
        })
      }
    }
  }
</script>
