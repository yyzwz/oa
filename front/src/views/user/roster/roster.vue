<style lang="less">
@import "../../../styles/table-common.less";
@import "./roster.less";
</style>
<template>
  <div class="search">
     <Row :gutter="32">
       <Split v-model="split1">
          <div slot="left" class="demo-split-pane">
            <Col>
              <Card>
                <Row v-show="openSearch" @keydown.enter.native="handleSearch">
                  <Form ref="searchForm" :model="searchForm" inline :label-width="40">
                    <Form-item label="姓名">
                      <Input
                        type="text"
                        v-model="searchForm.userName"
                        clearable
                        placeholder="请输入搜索的姓名"
                        style="width: 160px"
                      />
                    </Form-item>

                    <Form-item style="margin-left:-35px;" class="br">
                      <Button @click="add" type="success" icon="md-add">添加人员</Button>
                      <!-- <Button @click="delAll" icon="md-trash" type="error">批量删除</Button> -->
                      <Button @click="handleSearch" type="warning" icon="ios-search">人员搜索</Button>
                      <Button @click="handleReset" type="error" icon="ios-search">搜索重置</Button>
                      <Button @click="refresh" type="info" icon="ios-search">刷新页面</Button>
                      <!-- <Button @click="importAllUserByDingDing" type="primary" icon="md-add">钉钉导入</Button> -->
                    </Form-item>
                  </Form>
                </Row>
                <Row v-show="openTip">
                  <Alert show-icon style="width: 260px">
                    已选择
                    <span class="select-count">{{selectCount}}</span> 项
                    <a class="select-clear" @click="clearSelectAll">清空</a>
                  </Alert>
                </Row>
                <Row>
                  <Table
                    height="720"
                    style="width: 100%;"
                    :loading="loading"
                    border
                    :columns="columns"
                    :data="data"
                    sortable="custom"
                    @on-sort-change="changeSort"
                    @on-selection-change="showSelect"
                    @on-row-click="handleRow"
                    highlight-row
                    ref="table"
                  ></Table>
                </Row>
                <Row type="flex" justify="end" class="page">
                  <Page
                    :current="searchForm.pageNumber"
                    :total="total"
                    :page-size="searchForm.pageSize"
                    @on-change="changePage"
                    @on-page-size-change="changePageSize"
                    :page-size-opts="[20,30,50]"
                    size="small"
                    show-total
                    show-elevator
                    show-sizer
                  ></Page>
                </Row>
              </Card>
            </Col>
          </div>
          <div slot="right" class="demo-split-pane">
            <Collapse  accordion :value="xiala">
              <Panel name="a" class=" ivu-alert-info ivu-alert-with-icon">
                详细信息
                <div slot="content" >
                  <Col>
                    <card>
                      <div>
                        <Tabs :value="chooseName" @on-click="changeTab">
                          <TabPane label="基础信息" name="name1">
                            <user  v-bind:departmentName="departmentNameData"  @on-submit="getUserList"></user>
                          </TabPane>
                          <TabPane label="离复职" name="name2">
                            <status  v-bind:departmentName="departmentNameData"  @on-submit="getUserList"></status>
                          </TabPane>
                          <TabPane label="岗位信息" name="name3">
                            <gangWei  v-bind:departmentName="departmentNameData"  @on-submit="getUserList"></gangWei>
                          </TabPane>
                          <TabPane label="附件" name="name4">
                            <fuJian  v-bind:departmentName="departmentNameData"  @on-submit="getUserList"></fuJian>
                          </TabPane>
                          <TabPane label="自我介绍" name="name5">
                            <quanXian  v-bind:departmentName="departmentNameData"  @on-submit="getUserList"></quanXian>
                          </TabPane>
                          <TabPane label="权限预设" name="name6">
                            <yuShe  v-bind:departmentName="departmentNameData"  @on-submit="getUserList"></yuShe>
                          </TabPane>
                        </Tabs>
                      </div>
                    </card>
                  </Col>
                </div>
              </Panel>
            </Collapse> 
          </div>
        </Split>
     </Row>
    <addEdit :data="form" :type="showType" v-model="showUser" @on-submit="getUserList" />
  </div>
</template>

<script>
import {
  getDingUserListData,
  getHuaList,
  addHua,
  deleteHua,
  deleteDingDepartment
} from "./index";
import departmentChoose from "@/views/my-components/xboot/department-choose";
import { userColumns, userData } from "@/libs/importTemplate";
import addEdit from "./addEdit.vue";
import user from './user.vue';
import status from './status.vue';
import quanXian from './quanXian.vue';
import gangWei from './gangWei.vue'
import fuJian from './fuJian.vue'
import yuShe from './yuShe.vue'
export default {
  name: "jichu-manage",
  components: {
    departmentChoose,
    addEdit,
    user,
    status,
    quanXian,
    gangWei,
    fuJian,
    yuShe
  },
  data() {
    return {
      xiala:'b',
      split1: 0.3,
      chooseName:'name1',
      departmentNameData:'',
      selected:[ "部门名称", "部门ID","操作"],
      height: 510,
      showUser: false,
      showAddUser: false,
      showType: "0",
      openSearch: true,
      openTip: true,
      loading: true,
      reading: false,
      importLoading: false,
      loadingExport: true,
      exportModalVisible: false,
      importModalVisible: false,
      drop: false,
      selectCount: 0,
      selectList: [],
      searchForm: {
        pageNumber: 1,
        pageSize: 20,
      },
      selectDate: null,
      form: {},
      mycolumns:[],
      columns: [
        {
          title: '序号',
          width: 80,
          align: "center",
          fixed: "left",
          sortType:true,
          sortable: true,
          render: (h,params) => {
            return h('span',params.index + (this.searchForm.pageNumber-1)*this.searchForm.pageSize + 1 )
          }
        },
        {
          title: "姓名",
          key: "userName",
          minWidth: 50,
          align: "center",
          sortable: true,
        },
      ],
      chooseColumns: [],
      exportType: "",
      importTableData: [],
      importColumns: [],
      uploadfile: {
        name: ""
      },
      tempColumns: userColumns,
      tempData: userData,
      data: [],
      total: 0,
      dictSex: this.$store.state.dict.sex,
      showFilterPanelFlag: false
    };
  },
  methods: {
    init() {
      this.getUserList();
    },
    importAllUserByDingDing(){
      this.$Modal.confirm({
        title: "将钉钉人员数据同步到花名册",
        content: "该操作将需要等待10-20秒的时间！",
        loading: true,
        onOk: () => {
          getDingUserListData().then(res => {
            this.$Modal.remove();
            if (res.success) {
              this.$Message.success("导入钉钉人员至数据库成功");
              this.clearSelectAll();
              this.getUserList();
            }
          });
        }
      });
    },
    changeTab(value){
      this.chooseName = value;
    },
    handleRow(row, index) {
       this.departmentNameData = row.id;
       this.xiala = 'a';
    },
    handleSelectDep(v) {
      this.searchForm.departmentId = v;
    },
    changePage(v) {
      this.searchForm.pageNumber = v;
      this.getUserList();
      this.clearSelectAll();
    },
    changePageSize(v) {
      this.searchForm.pageSize = v;
      this.getUserList();
    },
    selectDateRange(v) {
      if (v) {
        this.searchForm.startDate = v[0];
        this.searchForm.endDate = v[1];
      }
    },
    getUserList() {
      this.loading = true;
      getHuaList(this.searchForm).then(res => {
        this.data = res.result.records;
        this.loading = false;
        this.total = res.result.total;
      });
    },
    handleSearch() {
      this.searchForm.pageNumber = 1;
      this.searchForm.pageSize = 20;
      this.getUserList();
    },
    handleReset() {
      this.searchForm.userName = "";
      this.searchForm.pageNumber = 1;
      this.searchForm.pageSize = 20;
      this.getUserList();
    },
    changeSort(e) {
      this.searchForm.sort = e.key;
      this.searchForm.order = e.order;
      if (e.order == "normal") {
        this.searchForm.order = "";
      }
      this.getUserList();
    },
    refresh() {
      this.getUserList();
    },
    clearImportData() {
      this.importTableData = [];
      this.importColumns = [];
      this.uploadfile = {};
    },
    showDetail(v) {
      for (let attr in v) {
        if (v[attr] == null) {
          v[attr] = "";
        }
      }
      let str = JSON.stringify(v);
      let data = JSON.parse(str);
      this.form = data;
      this.showType = "0";
      this.showUser = true;
    },
    add() {
      this.showType = "2";
      this.showUser = true;
    },
    addUser() {
      this.showType = "2";
      this.showAddUser = true;
    },
    edit(v) {
      for (let attr in v) {
        if (v[attr] == null) {
          v[attr] = "";
        }
      }
      let str = JSON.stringify(v);
      let data = JSON.parse(str);
      this.form = data;
      this.showType = "1";
      this.showUser = true;
    },
    editUser(v) {
      this.showType = "1";
      this.showAddUser = true;
    },
    remove(v) {
      this.$Modal.confirm({
        title: "确认删除",
        content: "您确认要删除该部门吗?",
        loading: true,
        onOk: () => {
          deleteDingDepartment({ids: v.id}).then(res => {
            this.$Modal.remove();
            if (res.success) {
              this.$Message.success("删除成功");
              this.getUserList();
            }
          });
        }
      });
    },
    showSelect(e) {
      this.exportData = e;
      this.selectList = e;
      this.selectCount = e.length;
    },
    clearSelectAll() {
      this.$refs.table.selectAll(false);
    },
    delAll() {
      if (this.selectCount <= 0) {
        this.$Message.warning("您还未选择要删除的数据");
        return;
      }
      this.$Modal.confirm({
        title: "确认删除",
        content: "您确认要删除所选的数据吗?",
        loading: true,
        onOk: () => {
          let ids = "";
          this.selectList.forEach(function(e) {
            ids += e.id + ",";
          });
          ids = ids.substring(0, ids.length - 1);
          deleteDingDepartment({ids: ids}).then(res => {
            this.$Modal.remove();
            if (res.success) {
              this.$Message.success("删除成功");
              this.clearSelectAll();
              this.getUserList();
            }
          });
        }
      });
    }
  },
  mounted() {
    this.height = Number(document.documentElement.clientHeight - 230);
    this.init();
    this.mycolumns=this.columns;
  }
  ,
  watch : {
      selected:function(newcolumns) {
          let showcolumns = [];
          for ( var i=0;i<this.mycolumns.length;i++) {
            var item=this.mycolumns[i];
            if(item.title==undefined)
              showcolumns.push(item);
            else 
               if(newcolumns.contains(item.title))
                  showcolumns.push(item);  
          }
          this.columns=showcolumns;
      }
    }
};
</script>
<style>
    .demo-split{
        height: 800px;
    }
    .demo-split-pane{
        padding: 10px;
    }
</style>