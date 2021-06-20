<style lang="less">
@import "../../../styles/tree-common.less";
</style>
<template>
  <div class="search">
      <Drawer title="导入客销经理" v-model="visible" closable width="1000">
         <car>
           <Input
              type="text"
              v-model="searchForm.nickname"
              clearable
              placeholder="请输入店名"
              style="width: 200px"
            />
            <Button @click="handleSearch" type="primary" icon="ios-search">搜索</Button>
            <Button @click="handleReset">重置</Button>
            <Row>
              <Table
                :loading="loading"
                border
                :columns="departmentArchiveColumns"
                :data="departmentArchiveData"
                sortable="custom"
                @on-sort-change="changeSort"
                @on-selection-change="showDepartmentArchiveSelect"
                ref="table"
              ></Table>
              
            </Row>
            <Row type="flex" justify="end" class="page">
              <Page
                :current="searchForm.currentPage"
                :total="searchForm.total"
                :page-size="searchForm.pageSize"
                @on-change="changeDepartmentArchivePage"
                @on-page-size-change="changeDepartmentArchivePageSize"
                :page-size-opts="[10,20,50]"
                size="small"
                show-total
                show-elevator
                show-sizer
              ></Page>
            </Row>
            <br>
            <Button @click="importInto" type="error">导入</Button>
            <Button @click="visible=false">退出</Button>
        </car>  
    </Drawer>
  </div>
</template>

<script>
import {
  getUserList
} from "./index";
export default {
  name: "profession-manage",
  data() {
    return {
      visible:this.value,
      loading: true,
      departmentArchiveData:[],
      selectData:[],
      departmentAchivePage: {
        currentPage:1,
        pageNumber: 1,
        pageSize: 10,
        totalNum:0
      },
      departmentArchiveColumns: [
        {
          title: "选择",
          type: "selection",
          minWidth: 60,
          align: "center"
        },
        {
          title: "姓名",
          key: "nickname",
          minWidth: 200,
          align: "center"
        },
        {
          title: "工号",
          key: "username",
          minWidth: 180,
          align: "center"
        }
      ],
      searchForm: {
        id: "",
        pageNumber: 1,
        pageSize: 10,
        sort: "createTime",
        order: "desc",
        total:0,
      },
    }
  },
  methods: {
    init() {
      this.getdepartmentArchiveData();
    },
    importInto(){
      this.$emit("import", this.selectData);
    },
    showDepartmentArchiveSelect(e){
      // console.log(e);
      this.selectData = e;
    },
    handleSearch(){
      this.getdepartmentArchiveData();
    },
    handleReset(){
      this.searchForm.nickname = "";
      this.getdepartmentArchiveData();
    },
    getdepartmentArchiveData(){
      var that = this;
      // console.log(this.searchForm);
      getUserList(this.searchForm).then(res => {
        that.departmentArchiveData = res.result.records;
        that.loading = false;
        this.searchForm.total=res.result.total;
        console.log("getUserList");
        console.log(res);
      })
    },
    changeDepartmentArchivePage(v) {
      //console.log(v);
      this.searchForm.pageNumber = v;
      this.getdepartmentArchiveData();
      this.clearSelectAll();
    },
    changeDepartmentArchivePageSize(v) {
      this.searchForm.pageSize = v;
      this.getdepartmentArchiveData();
    },
    clearSelectAll() {
      this.$refs.table.selectAll(false);
    },
    setCurrentValue(value) {
      if (value === this.visible) {
        return;
      }
      this.title = "导入客销经理";
      this.maxHeight = Number(document.documentElement.clientHeight - 121) + "px";
      this.visible = value;
    }
  },
  mounted() {
    // 计算高度
    let height = document.documentElement.clientHeight;
    this.maxHeight = Number(height - 287) + "px";
    this.init();
  },
  props: {
    value: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    visible(value) {
      this.$emit("input", value);
    },
    value(val) {
      this.setCurrentValue(val);
    },
  },
};
</script>