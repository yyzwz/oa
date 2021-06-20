<style lang="less">
@import "../../../styles/tree-common.less";
@import "../../../styles/common.less";
</style>
<template>
  <div class="search">
    <Card>
      <Row class="operation">
        <Button @click="add" type="success" icon="md-add">添加子部门</Button>
        <Button @click="addRoot" type="info" icon="md-add">添加一级部门</Button>
        <Button @click="delAll" type="error" icon="md-trash">批量删除</Button>
        <Button @click="getParentList" type="warning" icon="md-refresh">刷新</Button>
        <i-switch v-model="strict" size="large" style="margin-left:5px">
          <span slot="open">级联</span>
          <span slot="close">单选</span>
        </i-switch>
      </Row>
      <Row type="flex" justify="start">
        <Col :md="8" :lg="8" :xl="6" style="width:20%">
          <Alert show-icon>
            当前选择编辑部门：
            <p><span class="select-title">{{editTitle}}</span></p>
            <a class="select-clear" v-if="form.id" @click="cancelEdit">取消选择</a>
          </Alert>
          <Input
            v-model="searchKey"
            suffix="ios-search"
            @on-change="search"
            placeholder="输入部门名搜索"
            clearable
          />
          <div class="tree-bar" :style="{maxHeight: maxHeight}">
            <Tree
              ref="tree"
              :data="data"
              :load-data="loadData"
              show-checkbox
              @on-check-change="changeSelect"
              @on-select-change="selectTree"
              :check-strictly="strict"
            ></Tree>
            <Spin size="large" fix v-if="loading"></Spin>
          </div>
        </Col>
        <Col :md="15" :lg="13" :xl="9" style="margin-left:10px;width:79%">
            <Collapse accordion :value="collapseFlag">
              <Panel name = "1" class=" ivu-alert-info ivu-alert-with-icon">
                部门详细
                <div slot="content" >
                  <Form ref="form" :model="form" :label-width="100" :rules="formValidate">
                      <Row :gutter="32">
                        <Col span="14">
                            <FormItem label="上级部门" prop="parentTitle">
                              <div style="display:flex;">
                                <Input v-model="form.parentTitle" readonly style="margin-right:10px;; width:300px" />
                                <Poptip transfer trigger="click" placement="right-start" title="选择上级部门" width="250">
                                  <Button icon="md-list">选择部门</Button>
                                  <div slot="content" style="position:relative;min-height:5vh">
                                    <Tree :data="dataEdit" :load-data="loadData" @on-select-change="selectTreeEdit"></Tree>
                                    <Spin size="large" fix v-if="loadingEdit"></Spin>
                                  </div>
                                </Poptip>
                              </div>
                            </FormItem>
                          </col>
                        </row>
                        <Row :gutter="32">
                            <FormItem label="负责人" prop="mainHeaderId">
                              <Col span="12">
                              <Input v-model="form.mainHeaderName" clearable  readonly/>
                              </col>
                              <Col span="12">
                              <userChoose @on-change="getOneUserData"></userChoose>
                              </col>
                            </FormItem>
                        </row>
                        <Row :gutter="32"> 
                          <FormItem label="副负责人" prop="viceHeaderId">
                            <Col span="12">
                              <Input v-model="form.viceHeaderName" clearable  readonly/>
                            </Col>
                            <Col span="12">
                              <userChoose @on-change="getOneUserData2"></userChoose>
                            </Col>
                          </FormItem>
                        </row>
                        <Row :gutter="32">
                          <Col span="12">
                            <FormItem label="部门名称" prop="title">
                              <Input v-model="form.title" style="width:300px" />
                            </FormItem>
                          </col>
                        </row>
                        <Row :gutter="32">
                          <Col span="12">
                            <FormItem label="部门职责" prop="duty">
                              <Input v-model="form.duty" style="width:300px" />
                            </FormItem>
                          </col>
                        </row>
                        <Row :gutter="32">
                          <Col span="8">
                              <FormItem label="排序值" prop="sortOrder">
                                <Tooltip trigger="hover" placement="right" content="值越小越靠前，支持小数">
                                  <InputNumber :max="1000" :min="0" v-model="form.sortOrder"></InputNumber>
                                </Tooltip>
                              </FormItem>
                          </col>
                          <Col span="8">
                              <FormItem label="是否启用" prop="status">
                                <i-switch size="large" v-model="form.status" :true-value="0" :false-value="-1">
                                  <span slot="open">启用</span>
                                  <span slot="close">禁用</span>
                                </i-switch>
                              </FormItem>
                          </col>
                        </Row>
                        <Row :gutter="32">
                          <Col span="8">
                              <Form-item class="br">
                                <Button
                                  @click="submitEdit"
                                  :loading="submitLoading"
                                  type="primary"
                                  icon="ios-create-outline"
                                >修改并保存</Button>
                                <Button @click="handleReset">重置</Button>
                              </Form-item>
                          </col>
                      </Row>
                  </Form>
                </div>
              </Panel>
            </Collapse>
           <Divider class="divider"/>
          <div>
            <span style="color: red;">当前选择部门：{{form.title}}</span>
            </p>
            <car>
              <Row class="operation" style="position:relative;">
                <!-- <Button @click="userAdd" type="primary" icon="md-add">添加用户</Button> -->
                <Button @click="openImportRosterWindows" type="primary" icon="md-add">导入用户数据</Button>
              </Row>
              <Row>
                <Table
                  :loading="loading"
                  border
                  :columns="userColumns"
                  :data="users"
                  ref="departmentArchiveTable"
                ></Table>
              </Row>
              <Row type="flex" justify="end" class="page">
              <Page
                :current="userSearchForm.pageNumber"
                :total="userSearchFormTotal"
                :page-size="userSearchForm.pageSize"
                @on-change="changeUserPage"
                @on-page-size-change="changeUserPageSize"
                :page-size-opts="[10,20,50]"
                size="small"
                show-total
                show-elevator
                show-sizer
              ></Page>
            </Row>
            </car>
        </div>
        </Col>
      </Row>
    </Card>

    <Modal draggable="false"  :title="modalTitle" v-model="modalVisible" :closable="false" :mask-closable="false" :width="500">
      <Form ref="formAdd" :model="formAdd" :label-width="85" :rules="formValidate">
        <div v-if="showParent">
          <FormItem label="上级部门：">{{form.title}}</FormItem>
        </div>
        <FormItem label="部门名称" prop="title">
          <Input type="textarea"   :rows="7" v-model="formAdd.title" readonly="readonly" /> <Button type="primary" :loading="submitLoading" @click="getDepartmentArchiveList()">从部门档案导入</Button>
        </FormItem>
        <FormItem label="排序值" prop="sortOrder">
          <Tooltip trigger="hover" placement="right" content="值越小越靠前，支持小数">
            <InputNumber :max="1000" :min="0" v-model="formAdd.sortOrder"></InputNumber>
          </Tooltip>
        </FormItem>
        <FormItem label="是否启用" prop="status">
          <i-switch size="large" v-model="formAdd.status" :true-value="0" :false-value="-1">
            <span slot="open">启用</span>
            <span slot="close">禁用</span>
          </i-switch>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelAdd">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="submitAdd">提交</Button>
      </div>
    </Modal>
      <Drawer title="导入部门" closable v-model="showImportDepartmentArchiveManage" width="1000">
         <car>
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
                :current="departmentAchivePage.currentPage"
                :total="departmentAchivePage.totalNum"
                :page-size="departmentAchivePage.pageSize"
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
              <Button @click="showImportDepartmentArchiveManage=false">退出</Button>
        </car>  
      </Drawer>
      
      <addEditUser :department="form" :data="userInfo" :type="showUserType" v-model="showUser" @on-submit="getUserListInDepartment()" />
      <importRoster :selectDepartmentForm="form" v-model="showImportRoster" @on-submit="closeImportRosterWindows" />
  </div>
</template>

<script>
import {
  getHuaListData,
  getDepartmentArchiveListData,
} from "./api.js";
import {
  getAllDepartmentInTree,
  initDepartment,
  loadDepartment,
  addDepartment,
  editDepartment,
  deleteDepartment,
  searchDepartment,
  getUserByDepartmentId,
  deleteUser,
  enableUser,
  disableUser,
  getUserListData
} from "@/api/index";
import userChoose from './userChoose.vue';
import importDepartmentArchiveManage from "./addEditDepartmentArchive.vue";
import addEditUser from "./addEdit.vue";
import importRoster from './importRoster.vue';
export default {
  name: "department-manage",
  components: {
    userChoose,
    addEditUser,
    importRoster
  },
  data() {
    return {
      collapseFlag:'-1',
      showImportRoster:false,
      tempIds:'',
      nowSelectDepartmentId:'-1',
      isImportHuaData: false,
      userSearchForm: {
        id: "",
        nickname: "",
        username: "",
        departmentId: "",
        mobile: "",
        email: "",
        sex: "",
        type: "",
        status: "",
        pageNumber: 1,
        pageSize: 10,
        sort: "createTime",
        order: "desc",
        startDate: "",
        endDate: ""
      },
      huaselectCount: 0,
      departmentAchivePage: {
        currentPage:1,
        pageNumber: 1,
        pageSize: 10,
        totalNum:0
      },
      huasearchForm: {
        pageNumber: 1,
        pageSize: 10,
        username: ""
      },
      departmentArchiveTotal:0,
      tempData:'', // 用于选择部门时临时保存部门ID，方便删除用户后重新渲染列表
      showUser: false,
      showImportDepartmentArchiveManage:false,
      showUserType: "0",
      zwzTestData:[{"title":'未选择',"parentTitle":'未选择',"updateTime":'未选择'}],
      loading: true,
      hualoading: true,
      maxHeight: "500px",
      strict: true,
      userLoading: false,
      loadingEdit: true,
      modalVisible: false,
      selectList: [],
      selectCount: 0,
      departmentArchiveSelectList: [],
      departmentArchiveSelectCount: 0,
      showParent: false,
      modalTitle: "",
      editTitle: "",
      searchKey: "",
      form: {
        id: "",
        title: "",
        parentId: "",
        parentTitle: "",
        sortOrder: 0,
        status: 0
      },
      formAdd: {
        parentId: 0,
        sortOrder: 0,
        status: 0,
        title:""
      },
      formValidate: {
        title: [{ required: true, message: "名称不能为空", trigger: "blur" }],
        sortOrder: [
          {
            required: true,
            type: "number",
            message: "排序值不能为空",
            trigger: "blur"
          }
        ]
      },
      submitLoading: false,
      data: [],
      dataEdit: [],
      users: [],
      userInfo:{},
      departmentArchiveData:[],
      selectIndex:'',
      currentRowClassName:'',
      huaColumns: [
        {
          type: "selection",
          width: 55,
          align: "center",
          fixed: "left"
        },
        {
          title: "姓名",
          key: "username",
          width: 100,
          sortable: true
        },
        {
          title: "联系电话",
          key: "telpublic",
          width: 140,
          sortable: true
        },
        {
          title: "创建时间",
          key: "createTime",
          sortable: true,
          sortType: "desc",
          width: 200
        }
      ],
      departmentArchiveColumns: [
        {
          title: "选择",
          type: "selection",
          width: 60,
          align: "center",
          fixed: "left"
        },
        {
          title: "部门编号",
          key: "id",
          minWidth: 125,
          sortable: true
        },
        {
          title: "部门名称",
          key: "title",
          minWidth: 125,
          sortable: true
        },
        {
          title: "职责",
          key: "duty",
          minWidth: 125,
          sortable: true
        },
        {
          title: "状态",
          key: "status",
          align: "center",
          width: 110,
          render: (h, params) => {
            if (params.row.status == 0) {
              return h("div", [
                h("Badge", {
                  props: {
                    status: "success",
                    text: "正常启用"
                  }
                })
              ]);
            } else if (params.row.status == -1) {
              return h("div", [
                h("Badge", {
                  props: {
                    status: "error",
                    text: "禁用"
                  }
                })
              ]);
            }
          },
          filters: [
            {
              label: "正常启用",
              value: 0
            },
            {
              label: "禁用",
              value: -1
            }
          ],
          filterMultiple: false,
          filterRemote: e => {
            let v = "";
            if (e.length > 0) {
              v = e[0];
            }
            this.searchForm.status = v;
            this.searchForm.pageNumber = 1;
            this.getUserList();
          }
        },
        {
          title: "创建时间",
          key: "createTime",
          sortable: true,
          sortType: "desc",
          width: 180
        }
      ],
      userColumns: [
        {
          title: "登录账号",
          key: "username",
          minWidth: 125,
          sortable: true,
          align: "center",
        },
        {
          title: "用户名",
          key: "nickname",
          minWidth: 125,
          sortable: true,
          align: "center",
          render: (h, params) => {
            return h(
              "a",
              {
                on: {
                  click: () => {
                    this.showDetail(params.row);
                  }
                }
              },
              params.row.nickname
            );
          }
        },
        {
          title: "头像",
          key: "avatar",
          width: 80,
          align: "center",
          render: (h, params) => {
            return h("Avatar", {
              props: {
                src: params.row.avatar
              }
            });
          }
        },
        {
          title: "所属部门",
          key: "departmentTitle",
          minWidth: 120,
          align: "center",
        },
        {
          title: "手机",
          key: "mobile",
          minWidth: 125,
          sortable: true,
          align: "center",
        },
        {
          title: "邮箱",
          key: "email",
          minWidth: 180,
          sortable: true,
          align: "center",
        },
        {
          title: "性别",
          key: "sex",
          width: 70,
          align: "center"
        },
        {
          title: "类型",
          key: "type",
          align: "center",
          width: 100,
          render: (h, params) => {
            let re = "";
            if (params.row.type == 1) {
              re = "管理员";
            } else if (params.row.type == 0) {
              re = "普通用户";
            }
            return h("div", re);
          },
          filters: [
            {
              label: "普通用户",
              value: 0
            },
            {
              label: "管理员",
              value: 1
            }
          ],
          filterMultiple: false,
          filterRemote: e => {
            let v = "";
            if (e.length > 0) {
              v = e[0];
            }
            this.searchForm.type = v;
            this.searchForm.pageNumber = 1;
            this.getUserList();
          }
        },
        {
          title: "状态",
          key: "status",
          align: "center",
          width: 110,
          render: (h, params) => {
            if (params.row.status == 0) {
              return h("div", [
                h("Badge", {
                  props: {
                    status: "success",
                    text: "正常启用"
                  }
                })
              ]);
            } else if (params.row.status == -1) {
              return h("div", [
                h("Badge", {
                  props: {
                    status: "error",
                    text: "禁用"
                  }
                })
              ]);
            }
          },
          filters: [
            {
              label: "正常启用",
              value: 0
            },
            {
              label: "禁用",
              value: -1
            }
          ],
          filterMultiple: false,
          filterRemote: e => {
            let v = "";
            if (e.length > 0) {
              v = e[0];
            }
            this.searchForm.status = v;
            this.searchForm.pageNumber = 1;
            this.getUserList();
          }
        },
        {
          title: "创建时间",
          key: "createTime",
          sortable: true,
          sortType: "desc",
          width: 180
        },
        {
          title: "操作",
          key: "action",
          width: 200,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            let enableOrDisable = "";
            if (params.row.status == 0) {
              enableOrDisable = h(
                "Button",
                {
                  props: {
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.disable(params.row);
                    }
                  }
                },
                "禁用"
              );
            } else {
              enableOrDisable = h(
                "Button",
                {
                  props: {
                    type: "success",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.enable(params.row);
                    }
                  }
                },
                "启用"
              );
            }
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.userEdit(params.row);
                    }
                  }
                },
                "编辑"
              ),
              enableOrDisable,
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small"
                  },
                  on: {
                    click: () => {
                      this.userRemove(params.row);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      columns: [
        {
          type: "selection",
          width: 60,
          align: "center"
        },
        {
          type: "index",
          width: 60,
          align: "center"
        },
        {
          title: "部门名称",
          key: "title",
          minWidth: 120,
          sortable: true,
          tree: true
        },
        {
          title: "排序",
          key: "sortOrder",
          width: 150,
          sortable: true,
          align: "center",
          sortType: "asc"
        },
        {
          title: "创建时间",
          key: "createTime",
          sortable: true,
          width: 200
        },
        {
          title: "操作",
          key: "action",
          width: 300,
          align: "center",
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small",
                    icon: "md-add"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.tableAdd(params.row);
                    }
                  }
                },
                " 添加子部门"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small"
                  },
                  on: {
                    click: () => {
                      this.remove(params.row);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ]
    };
  },
  methods: {
    init() {
      this.getParentList();
      this.getParentListEdit();
      this.getUserListInDepartment();
    },
    getOneUserData2(e){
      this.form.viceHeaderId = e.id;
      this.form.viceHeaderName = e.nickname;
    },
    getOneUserData(e){
      this.form.mainHeaderId = e.id;
      this.form.mainHeaderName = e.nickname;
    },
    openImportRosterWindows(){
      this.showImportRoster = true;
    },
    closeImportRosterWindows(){
      this.showImportRoster = false;
      this.init();
    },
    getUserListInDepartment(){
       getUserListData(this.userSearchForm).then(res => {
        this.userLoading = false;
        if (res.success) {
          this.users = res.result.records;
          this.userSearchFormTotal = res.result.total;
        }
      });
    },
    changeUserPage(v) {
      this.userSearchForm.pageNumber = v;
      this.getUserListInDepartment();
    },
    changeUserPageSize(v) {
      this.userSearchForm.pageSize = v;
      this.getUserListInDepartment();
    },
    changeDepartmentArchivePage(v) {
      this.departmentAchivePage.pageNumber = v;
      this.getDepartmentArchiveList();
      this.clearSelectAll();
    },
    changeDepartmentArchivePageSize(v) {
      this.departmentAchivePage.pageSize = v;
      this.getDepartmentArchiveList();
    },
    getDepartmentArchiveList() {
      // 多条件搜索部门列表
      getDepartmentArchiveListData(this.departmentAchivePage).then(res => {
        if (res.success) {
          this.departmentArchiveData = res.result.records;
          this.showImportDepartmentArchiveManage=true;
          this.departmentAchivePage.totalNum=res.result.total;
        }
      });
    },
    getParentList() {
      this.loading = true;
      getAllDepartmentInTree().then(res => {
        this.loading = false;
        if (res.success) {
          res.result.forEach(function(e) {
            if (e.isParent) {
              e.loading = false;
              //e.children = [];
              e._loading = false;
            }
          });
          this.data = res.result;
        }
      });
    },
    getParentListEdit() {
      this.loadingEdit = true;
      initDepartment().then(res => {
        this.loadingEdit = false;
        if (res.success) {
          res.result.forEach(function(e) {
            if (e.isParent) {
              e.loading = false;
              e.children = [];
            }
          });
          // 头部加入一级
          let first = {
            id: "0",
            title: "一级部门"
          };
          res.result.unshift(first);
          this.dataEdit = res.result;
        }
      });
    },
    loadData(item, callback) {
      loadDepartment(item.id).then(res => {
        if (res.success) {
          res.result.forEach(function(e) {
            if (e.isParent) {
              e.loading = false;
              e.children = [];
              e._loading = false;
            }
          });
          callback(res.result);
        }
      });
    },
    search() {
      if (this.searchKey) {
        this.loading = true;
        searchDepartment({ title: this.searchKey }).then(res => {
          this.loading = false;
          if (res.success) {
            this.data = res.result;
          }
        });
      } else {
        this.getParentList();
      }
    },
    selectTree(v) {
      if(v==null||v==undefined||v.length==0){
          this.cancelEdit();
          return;
      }
      this.collapseFlag = "1";
      this.nowSelectDepartmentId = v[0].id;
      if (v.length > 0) {
        // 转换null为""
        for (let attr in v[0]) {
          if (v[0][attr] == null) {
            v[0][attr] = "";
          }
        }
        let str = JSON.stringify(v[0]);
        let data = JSON.parse(str);
        this.editTitle = data.title;
        // 加载部门用户数据
        this.userLoading = true;
        this.tempData = data;
        this.userSearchForm.departmentId=data.id;
        this.getUserListInDepartment();
        // 回显
        this.form = data;
      } else {
        this.cancelEdit();
      }
    },
    cancelEdit() {
      let data = this.$refs.tree.getSelectedNodes()[0];
      if (data) {
        data.selected = false;
      }
      //this.form = data;
      this.$refs.form.resetFields();
      this.form.id="";
      this.editTitle = "";
      this.userSearchForm.departmentId="";
      this.form="";
      this.getUserListInDepartment();
    },
    selectTreeEdit(v) {
      if (v.length > 0) {
        // 转换null为""
        for (let attr in v[0]) {
          if (v[0][attr] == null) {
            v[0][attr] = "";
          }
        }
        let str = JSON.stringify(v[0]);
        let data = JSON.parse(str);
        this.form.parentId = data.id;
        this.form.parentTitle = data.title;
      }
    },
    cancelAdd() {
      this.modalVisible = false;
      this.showImportDepartmentArchiveManage=false;
    },
    handleReset() {
      this.$refs.form.resetFields();
      this.form.status = 0;
    },
    handleRow(row, index) {
      this.selectIndex=index;
      getUserByDepartmentId(row.id).then(res => {
        this.userLoading = false;
        if (res.success) {
          this.users = res.result;
          // 回显
          this.form = row;
        }
      });
    },
    // 添加样式
    rowName (row, index) {
      this.selectIndex=row.id;
      if (row.id == this.selectIndex) {
          return 'demo_table';
      }
    },
    showSelect(e) {
      this.selectList = e;
      this.selectCount = e.length;
    },
    showDepartmentArchiveSelect(e) {      
      this.tempIds = "";
      for(var j = 0 ; j < e.length ; j ++){
        if(j == 0) this.tempIds += e[j].id;
        else this.tempIds += "," + e[j].id;
      }
      this.formAdd.ids=this.tempIds;
      this.formAdd.title='';
      this.departmentArchiveSelectList = e;
      this.departmentArchiveSelectCount = e.length;
      for( var i=0;i<this.departmentArchiveSelectList.length;i++){
        var tempDepartmentArchive=this.departmentArchiveSelectList[i];
        if(i!=0){
          this.formAdd.title=this.formAdd.title+";"+tempDepartmentArchive.title;
          // this.formAdd.zhize=this.formAdd.zhize+";"+tempDepartmentArchive.zhize;
        }
        else{
          this.formAdd.title=this.formAdd.title+tempDepartmentArchive.title;
          // this.formAdd.zhize=this.formAdd.tizhizetle+tempDepartmentArchive.zhize;
        }
      }
    },
    clearSelectAll() {
      this.$refs.table.selectAll(false);
    },
    submitEdit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (!this.form.id) {
            this.$Message.warning("请先点击选择要修改的部门");
            return;
          }
          this.submitLoading = true;
          editDepartment(this.form).then(res => {
            this.submitLoading = false;
            if (res.success) {
              this.$Message.success("编辑成功");
              this.init();
              this.modalVisible = false;
            }
          });
        }
      });
    },
    submitAdd() {
      // this.$refs.formAdd.validate(valid => {
      //   if (valid) {
          this.submitLoading = true;
          this.formAdd.tempIds = this.tempIds;
          this.formAdd.parentDepartmentId = this.form.id;
          addDepartment(this.formAdd).then(res => {
            this.submitLoading = false;
            if (res.success) {
              this.$Message.success("添加成功");
              this.init();
              this.modalVisible = false;
              this.showImportDepartmentArchiveManage=false;
            }
          });
      //   }
      // });
    },
    tableAdd(v) {
      this.form = v;
      this.add();
    },
    add() {
      if (this.form.id == "" || this.form.id == null) {
        this.$Message.warning("请先点击选择一个部门");
        return;
      }
      this.modalTitle = "添加子部门";
      this.showParent = true;
      this.formAdd = {
        parentId: this.form.id,
        sortOrder: 0,
        status: 0,
        title:""
      };
      this.modalVisible = true;
    },
    addRoot() {
      this.modalTitle = "添加一级部门";
      this.showParent = false;
      this.formAdd = {
        parentId: 0,
        sortOrder: 0,
        status: 0,
        title:""
      };
      this.modalVisible = true;
    },
  
    changeSelect(v) {
      this.selectCount = v.length;
      this.selectList = v;
    },
    remove(v) {
      this.selectCount = 1;
      this.selectList.push(v);
      this.delAll();
    },
    delAll() {
      if (this.selectCount <= 0) {
        this.$Message.warning("您还未勾选要删除的数据");
        return;
      }
      this.$Modal.confirm({
        title: "确认删除",
        content:
          "您确认要删除所选的 " + this.selectCount + " 条数据及其下级所有数据?",
        loading: true,
        onOk: () => {
          let ids = "";
          this.selectList.forEach(function(e) {
            ids += e.id + ",";
          });
          ids = ids.substring(0, ids.length - 1);
          deleteDepartment({ids: ids}).then(res => {
            this.$Modal.remove();
            if (res.success) {
              this.$Message.success("删除成功");
              this.selectList = [];
              this.selectCount = 0;
              this.cancelEdit();
              this.init();
            }
          });
        }
      });
    },
    userEdit(v) {
      // 转换null为""
      for (let attr in v) {
        if (v[attr] == null) {
          v[attr] = "";
        }
      }
      let str = JSON.stringify(v);
      let data = JSON.parse(str);
      this.userInfo = data;
      this.showUserType = "1";
      this.showUser = true;
    },
    userAdd(v) {
      //this.userInfo.departmentTitle=this.form.title;
      let departmentIds = [];
      departmentIds.push(this.form.id);
      this.userInfo={
          status:"0" ,
          del_flag:"0" ,
          createBy: "",
          createTime: "",
          updateBy:"",
          updateTime: "",
          delFlag: "",
          hua:"",
          fujian: "",
          username:"",
          password:"",
          nickname: "",
          mobile:"",
          email: "",
          address:"",
          street: "",
          sex: "",
          passStrength: "",
          avatar: "",
          type: "",
          status: "",
          description: "",
          departmentId: this.form.id,
          departmentTitle: this.form.title,
          position: "",
          workplace: "",
          telsmall: "",
          birth: "",
          departmentIds: departmentIds,
          departments: "",
          defaultRole: "",
          roles:[],
          roleIds:[]
       };
       this.showUserType = "4";
       this.showUser = true;
    },
    userRemove(v) {
      this.$Modal.confirm({
        title: "确认删除",
        content:
          "您确认要删除所选的数据?",
        loading: true,
        onOk: () => {
          this.$Modal.remove();
          deleteUser({ids:v.id}).then(res => {
            if (res.success) {
              this.$Message.success("删除成功");
              //this.selectList = [];
              //this.selectCount = 0;
              //this.cancelEdit();
              this.getUserListInDepartment();
            }
          })
          // 刷新数据
          getUserByDepartmentId(this.tempData.id).then(res => {
            this.userLoading = false;
            if (res.success) {
              this.users = res.result;
              this.form = this.tempData;
            }
          });
        }
      })
    },
    enable(v) {
      this.$Modal.confirm({
        title: "确认启用",
        content: "您确认要启用用户 " + v.username + " ?",
        loading: true,
        onOk: () => {
          enableUser(v.id).then(res => {
            this.$Modal.remove();
            if (res.success) {
              // 刷新数据
              this.getUserListInDepartment();
              this.$Message.success("操作成功");
            }
          });

        }
      });
    },
    disable(v) {
      this.$Modal.confirm({
        title: "确认禁用",
        content: "您确认要禁用用户 " + v.username + " ?",
        loading: true,
        onOk: () => {
          disableUser(v.id).then(res => {
            this.$Modal.remove();
            if (res.success) {
              // 刷新数据
              this.getUserListInDepartment();
              this.$Message.success("操作成功");
            }
          });
          
        }
      });
    },
  },
  mounted() {
    // 计算高度
    let height = document.documentElement.clientHeight;
    this.maxHeight = Number(height - 287) + "px";
    this.init();
  }
};
</script>