<style lang="less">
@import "../../../styles/table-common.less";
@import "./roleManage.less";
</style>
<template>
  <div>
   <span style="color:#f00;font-size:24px;">以下用户拥有 {{perssiontitle}} 权限</span>
    <Card>
      <Row>
        <Table
          :loading="loading"
          border
          :columns="columns"
          :data="data"
          ref="table"
          sortable="custom"
        ></Table>
      </Row>
      <Row type="flex" justify="end" class="page">
        <Page
          :current="pageNumber"
          :total="total"
          :page-size="pageSize"
          @on-change="changePage"
          @on-page-size-change="changePageSize"
          :page-size-opts="[10,20,50]"
          size="small"
          show-total
          show-elevator
          show-sizer
        ></Page>
      </Row>
    </Card>
  </div>
</template>

<script>
import {
  getUserByPermission
} from "./index";
export default {
  name: "role-manage",
  data() {
    return {
      perssiontitle:'',
      loading: false,
      data: [],
      pageNumber: 1,
      pageSize: 10,
      total: 0,
      perssionid:'未选择',
      columns: [
        {
          type: "index",
          minWidth: 30,
          align: "center"
        },
        {
          title: "工号",
          key: "username",
          minWidth: 50,
        },
        {
          title: "姓名",
          key: "nickname",
          minWidth: 50,
        },
        {
          title: "权限角色",
          key: "roleName",
          minWidth: 90,
        },
        {
          title: "部门",
          key: "departmentTitle",
          minWidth: 210,
        }
      ],
    };
  },
  
  props: ['perid','pertitle'],
  watch:{
      perid:function(newVal,oldVal){
        // console.log("newVal = " + newVal);
        this.perssionid = newVal;
        this.getDataList();
      },
      pertitle:function(newVal,oldVal){
        console.log(newVal);
        this.perssiontitle = newVal;
      }
  },
  methods: {
    init() {
    },
    getDataList(){
      var that = this;
      if(this.perssionid == '未选择'){
        return ;
      }
      that.loading = true;
      getUserByPermission({pid:this.perssionid}).then(res => {
        // console.log(res);
        that.data = res.result.records;
        that.loading = false;
      });
    },
    changePage(v) {
      this.pageNumber = v;
      this.getDataList();
    },
    changePageSize(v) {
      this.pageSize = v;
      this.getDataList();
    },
  },
  mounted() {
    this.init();
  },
  created(){
  },
  destroyed(){
  },
};
</script>