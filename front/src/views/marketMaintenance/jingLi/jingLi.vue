<style lang="less">
@import "../../../styles/tree-common.less";
</style>
<template>
  <div class="search">
    <Card>
      <Row class="operation">
        <Button @click="add" type="primary" icon="md-add" v-show="showType=='tree'">添加经理</Button>
        <Button type="warning" @click="addRoot" icon="md-add">添加一级经理</Button>
        <Button type="error" @click="delAll" icon="md-trash">批量删除</Button>
        <Button type="info" @click="getParentList" icon="md-refresh">刷新</Button>
        <Input
          v-model="searchKey"
          suffix="ios-search"
          @on-change="search"
          placeholder="输入经理名称搜索"
          clearable
          style="width: 250px"
          v-show="showType=='list'"
        />
        <i-switch v-model="strict" size="large" v-show="showType=='tree'" style="margin-left:5px">
          <span slot="open">级联</span>
          <span slot="close">单选</span>
        </i-switch>
        <div style="float: right">
          <RadioGroup v-model="showType" type="button">
            <Radio title="树结构" label="tree">
              <Icon type="md-list"></Icon>
            </Radio>
            <Radio title="列表" label="list">
              <Icon type="ios-apps"></Icon>
            </Radio>
          </RadioGroup>
        </div>
      </Row>
      <Row type="flex" justify="start" v-show="showType=='tree'" style="width:100%">
        <Col style="width:25%">
          <Alert show-icon>
            当前选择编辑：
            <span class="select-title">{{editTitle}}</span>
            <a class="select-clear" v-if="form.id" @click="cancelEdit">取消选择</a>
          </Alert>
          <Input
            v-model="searchKey"
            suffix="ios-search"
            @on-change="search"
            placeholder="输入经理名称搜索"
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
        <Col style="width:70%; margin-left:2%;">
          <Collapse accordion >
            <Panel  class=" ivu-alert-info ivu-alert-with-icon">
              客销详细
              <div slot="content" >
                <Form ref="form" :model="form" :label-width="100" :rules="formValidate">
                  <Row>
                    <Col span="12">
                      <FormItem label="上级经理" prop="parentTitle">
                        <div style="display:flex;">
                          <Input v-model="form.parentTitle" readonly style="margin-right:10px;" />
                          <Poptip transfer trigger="click" placement="right-start" title="选择上级经理" width="250">
                            <Button icon="md-list">选择经理</Button>
                            <div slot="content" style="position:relative;min-height:5vh">
                              <Tree :data="dataEdit" :load-data="loadData" @on-select-change="selectTreeEdit"></Tree>
                              <Spin size="large" fix v-if="loadingEdit"></Spin>
                            </div>
                          </Poptip>
                        </div>
                      </FormItem>
                    </Col>
                    <Col span="12">
                      <FormItem label="工号" >
                        <Input v-model="form.jobnumber" />
                      </FormItem>
                    </Col>
                  </Row>
                  <Row>
                    <Col span="12">
                      <FormItem label="手机号" prop="title">
                        <Input v-model="form.mobile" />
                      </FormItem>
                    </Col>
                    <Col span="12">
                      <FormItem label="排序值" prop="sortOrder">
                        <Tooltip trigger="hover" placement="right" content="值越小越靠前，支持小数">
                          <InputNumber :max="1000" :min="0" v-model="form.sortOrder"></InputNumber>
                        </Tooltip>
                      </FormItem>
                    </Col>
                  </Row>
                  <Row>
                    <Col span="12">
                      <FormItem label="备注">
                        <Input v-model="form.remark" />
                      </FormItem>
                    </Col>
                    <Col span="12">
                      <FormItem label="是否启用" prop="status">
                        <i-switch size="large" v-model="form.status" :true-value="0" :false-value="-1">
                          <span slot="open">启用</span>
                          <span slot="close">禁用</span>
                        </i-switch>
                      </FormItem>
                    </Col>
                  </Row>
                  
                  <Row>
                    <Col span="12">
                    <FormItem label="创建人">
                      <Input v-model="form.createBy" readonly style="width:200px;"  />
                    </FormItem>
                    </Col>
                    <Col span="12">
                    <FormItem label="创建时间" >
                      <Input v-model="form.createTime" readonly style="width:200px" />
                    </FormItem>
                    </Col>
                  </Row>
                  <Row>
                    <Col span="12">
                      <FormItem label="最后修改人">
                        <Input v-model="form.updateBy" readonly style="width:200px" />
                      </FormItem>
                    </Col>
                    <Col span="12">
                      <FormItem label="修改时间">
                        <Input v-model="form.updateTime" readonly style="width:200px" />
                      </FormItem>
                    </Col>
                  </Row>
                  <Form-item class="br">
                    <Button
                      @click="submitEdit"
                      :loading="submitLoading"
                      type="primary"
                      icon="ios-create-outline"
                    >修改并保存</Button>
                    <Button @click="handleReset">重置</Button>
                  </Form-item>
                </Form>
              </div>
            </Panel>
          </Collapse>
          <Divider class="divider"/>
            <div>
              <car>
              <Row class="operation" :gutter="32">
                <Col span="8">
                  <Button  @click="openImportHuaWindows" type="primary" icon="md-add">导入店面数据</Button>
                </Col>
                <!-- <Col span="16">
                  <Form ref="userSearchForm" :model="userSearchForm" inline labelWidth="70">
                    <Form-item label="店名">
                      <Input
                        type="text"
                        v-model="userSearchForm.title"
                        clearable
                        placeholder="请输入店名"
                        style="width: 200px"
                      />
                    </Form-item>
                    <Button @click="f5" type="error">搜索</Button>
                    <Button @click="chongzhi" type="primary">重置</Button>
                  </Form>
                </Col> -->
                
              </Row>
              <Row>
                
                <Table
                  :loading="loading"
                  border
                  :columns="userColumns"
                  :data="DianList"
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
      <Alert show-icon v-show="showType=='list'">
        已选择
        <span class="select-count">{{selectCount}}</span> 项
        <a class="select-clear" @click="clearSelectAll">清空</a>
      </Alert>
      <Table
        row-key="title" 
        :load-data="loadData"
        :columns="columns"
        :data="data"
        :loading="loading"
        border
        ref="table"
        @on-selection-change="showSelect"
        v-if="showType=='list'"
      ></Table>
    </Card>

    <Modal :title="modalTitle" v-model="modalVisible" :mask-closable="false" :width="500" draggable="false" >
      <Form ref="formAdd" :model="formAdd" :label-width="85" :rules="formValidate">
        <div v-if="showParent">
          <FormItem label="上级经理：">{{form.title}}</FormItem>
        </div>
        <FormItem label="客销经理" prop="title">
          <Input v-model="formAdd.title" />
          <Button type="primary" :loading="submitLoading" @click="getDepartmentArchiveList">从花名册导入</Button>
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
    
    <hua v-model="showHuaFlag" @import="importUserData"></hua>
    <Drawer title="导入店面数据" closable v-model="showDianHuaFlag" width="1000">
      <car>
        <Row>
          <Form ref="huasearchForm" :model="huasearchForm" inline :label-width="70">
            <Form-item label="店名">
              <Input
                type="text"
                v-model="huasearchForm.dianName"
                clearable
                placeholder="请输入店名"
                style="width: 200px"
              />
            </Form-item>
            <Form-item>
              <Button @click="huahandleSearch" type="primary" icon="ios-search">搜索</Button>
              <Button @click="huahandleReset">重置</Button>
            </Form-item>
          </Form>
        </Row>
        <Row>
          <Alert show-icon>
            已选择
            <span class="select-count">{{huaselectCount}}</span> 项
            <a class="select-clear" @click="huaclearSelectAll">清空</a>
          </Alert>
        </Row>
        <Row>
          <Table
            :loading="hualoading"
            border
            :columns="huaColumns"
            :data="huaData"
            sortable="custom"
            @on-sort-change="huachangeSort"
            @on-selection-change="huashowHuaSelect"
            ref="table1"
          ></Table>
          <br>
        </Row>
        <Row type="flex" justify="end" class="page">
          <Page
            :current="huasearchForm.pageNumber"
            :total="huaTotal"
            :page-size="huasearchForm.pageSize"
            @on-change="huachangePage"
            @on-page-size-change="huachangePageSize"
            :page-size-opts="[10,20,50]"
            size="small"
            show-total
            show-elevator
            show-sizer
          ></Page>
        </Row>
        <Button type="primary" @click="huaAddData">从花名册导入</Button> &nbsp;
        <Button @click="showDianHuaFlag=false">退出</Button>
      </car>
    </Drawer>
  </div>
</template>

<script>
import {
  importMore,
  getDianByNotImport,
  deleteJAD,
  getDianList,
  importJingLiById,
  getAllJueSeMangerInTree,
  initJueSeManger,
  loadJueSeManger,
  addJueSeManger,
  editJueSeManger,
  deleteJueSeManger,
  searchJueSeManger
} from "./index";
import hua from './hua.vue';
export default {
  components: {
    hua
  },
  name: "profession-manage",
  data() {
    return {
      userSearchFormTotal:'',
      huaData:[],
      showDianHuaFlag:false,
      showHuaFlag:false,
      showType: "tree",
      loading: true,
      maxHeight: "500px",
      strict: true,
      userLoading: false,
      loadingEdit: true,
      modalVisible: false,
      selectList: [],
      selectCount: 0,
      showParent: false,
      modalTitle: "",
      editTitle: "",
      searchKey: "",
      huasearchForm: {
        pageNumber: 1,
        pageSize: 10,
        dianName: ""
      },
      hualoading: false,
      huaColumns: [
        {
          type: "selection",
          width: 55,
          align: "center",
          fixed: "left"
        },
        {
          title: "店名",
          key: "dianName",
          minWidth: 200
        },
        {
          title: "省份",
          key: "sheng",
          minWidth: 100
        },
        {
          title: "城市",
          key: "city",
          minWidth: 100
        },
        {
          title: "县区",
          key: "xian",
          minWidth: 100
        }
      ],
      huaselectCount: 0,
      userColumns: [
        {
          title: "店名",
          align: "center",
          key: "dianName",
          minWidth: 125
        },{
          title: "分公司",
          align: "center",
          key: "sonCompany",
          minWidth: 125
        },{
          title: "大区",
          align: "center",
          key: "bigArea",
          minWidth: 125
        },{
          title: "区域",
          align: "center",
          key: "area",
          minWidth: 125
        },{
          title: "省份",
          align: "center",
          key: "sheng",
          minWidth: 125
        },{
          title: "城市",
          align: "center",
          key: "city",
          minWidth: 125
        },{
          title: "县区",
          align: "center",
          key: "xian",
          minWidth: 125
        },{
          title: "法人",
          align: "center",
          key: "faPeople",
          minWidth: 125
        },{
          title: "详细地址",
          align: "center",
          key: "address",
          minWidth: 125
        },{
          title: "负责人",
          align: "center",
          key: "fuZe",
          minWidth: 125
        },
        {
          title: "操作",
          key: "action",
          minWidth: 100,
          align: "center",
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small"
                  },
                  on: {
                    click: () => {
                      this.removeDian(params.row);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      form: {
        id: "",
        title: "",
        parentId: "",
        parentTitle: "",
        sortOrder: 0,
        status: 0,
        remark:"",
        requirement:""
      },
      userSearchForm: {
        id: "",
        title: "",
        jobnumber: "",
        mobile: "",
        pageNumber: 1,
        pageSize: 10,
        sort: "createTime",
        order: "desc"
      },
      formAdd: {},
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
      selectItem:{},
      submitLoading: false,
      data: [],
      dataEdit: [],
      users: [],
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
          title: "经理名称",
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
           if (this.$route.meta.permTypes.includes("add")){
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
                " 添加子角色"
              )
            ]);
            }else if (this.$route.meta.permTypes.includes("delete")){
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
                " 添加子角色"
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
        }
      ],
      isImportHuaData:false,
      DianList:[],
      tempSelectHuaData:[],
    };
  },
  methods: {
    init() {
      this.getParentList();
      this.getParentListEdit();
    },
    huaAddData(){
      var that = this;
      let ids = "";
      for(var i = 0 ; i < this.tempSelectHuaData.length ; i ++){
        ids += this.tempSelectHuaData[i].id + ",";
      }
      ids = ids.substring(0, ids.length - 1);
      console.log(ids);
      importMore({ids:ids,pid:this.selectItem.id}).then(res => {
        console.log(res);
        that.getDianByNotImportFx();
        that.init();
        that.showDianHuaFlag = false;
      })
    },
    removeDian(e){
      var that = this;
      this.$Modal.confirm({
        title: "确认删除",
        content:"您确认要删除?",
        loading: true,
        onOk: () => {
          deleteJAD({pid:this.selectItem.id,did:e.id}).then(res => {
            this.$Modal.remove();
            this.$Message.success("删除成功");
            that.getHuaUserList();
          })
        }
      });
    },
    huashowHuaSelect(e) {
      // console.log(e);
      this.tempSelectHuaData = e;
      // this.huaselectList = e;
      // this.huaselectCount = e.length;
    },
    openImportHuaWindows(){
      var that = this;
      this.huasearchForm.pid = this.selectItem.id;
        // this.getHuaUserList();
        // this.isImportHuaData=true;
        this.showDianHuaFlag = true;
        this.getDianByNotImportFx();
    },
    getDianByNotImportFx(){
      var that = this;
      getDianByNotImport(this.huasearchForm).then(res => {
          // console.log("getDianByNotImport");
          // console.log(res);
          that.huaData = res.result.records;
          that.userSearchFormTotal = res.result.total;
        })
    },
    changeUserPage(v) {
      this.userSearchForm.pageNumber = v;
      this.getDianByNotImportFx();
    },
    changeUserPageSize(v) {
      this.userSearchForm.pageSize = v;
      this.getDianByNotImportFx();
    },
    huahandleSearch(){
      this.openImportHuaWindows();
    },
    getHuaUserList(){
      // console.log(this.selectItem.id);
      getDianList({id:this.selectItem.id}).then(res => {
        // console.log(res);
        this.DianList = res.result;
      })
    },
    f5(){
      this.getHuaUserList();
    },
    importUserData(e){
      var that = this;
      // console.log(e);
      this.showHuaFlag = false;
      this.modalVisible = false;
      let ids = "";
      e.forEach(function(ee) {
        ids += ee.id + ",";
      });
      ids = ids.substring(0, ids.length - 1);
      importJingLiById({ids:ids,pid:that.formAdd.parentId}).then(res => {
        // console.log("importJingLiById");
        // console.log(res);
        this.$Message.success("导入成功");
        this.init();
      })
    },
    huaclearSelectAll() {
      this.$refs.table1.selectAll(false);
    },
    getDepartmentArchiveList() {
      // console.log("getDepartmentArchiveList");
      this.showHuaFlag = true;
      // getDepartmentArchiveListData(this.departmentAchivePage).then(res => {
      //   if (res.success) {
      //     this.departmentArchiveData = res.result.content;
      //     this.showImportDepartmentArchiveManage=true;
      //     this.departmentAchivePage.totalNum=res.result.totalElements;
      //   }
      // });
    },
    getParentList() {
      this.loading = true;
      getAllJueSeMangerInTree().then(res => {
        this.loading = false;
        if (res.success) {
          res.result.forEach(function(e) {
            if (e.isParent) {
              e.loading = false;
              //e.children1 = [];
              e._loading = false;
              // e.expand = false;//保证选择岗位时，树形岗位不展开
            }
          });
          this.data = res.result;
        }
      });
    },
    getParentListEdit() {
      this.loadingEdit = true;
      initJueSeManger().then(res => {
        this.loadingEdit = false;
        if (res.success) {
          res.result.forEach(function(e) {
            if (e.isParent) {
              e.loading = false;
              e.children1 = [];
              e.expand = false;//保证选择岗位时，树形岗位不展开
            }
          });
          // 头部加入一级
          let first = {
            id: "0",
            title: "一级经理"
          };
          res.result.unshift(first);
          this.dataEdit = res.result;
        }
      });
    },
    loadData(item, callback) {
      loadJueSeManger(item.id).then(res => {
        if (res.success) {
          res.result.forEach(function(e) {
            if (e.isParent) {
              e.loading = false;
              e.children1 = [];
              e._loading = false;
              e.expand = false;//保证选择岗位时，树形岗位不展开
            }
          });
          callback(res.result);
        }
      });
    },
    search() {
      if (this.searchKey) {
        this.loading = true;
        searchJueSeManger({ title: this.searchKey }).then(res => {
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
      // console.log(v);
      this.selectItem = v[0];
      this.getHuaUserList();
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
        // 加载经理用户数据
        this.userLoading = true;
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
      this.$refs.form.resetFields();
      delete this.form.id;
      this.editTitle = "";
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
    },
    handleReset() {
      this.$refs.form.resetFields();
      this.form.status = 0;
    },
    showSelect(e) {
      this.selectList = e;
      this.selectCount = e.length;
    },
    clearSelectAll() {
      this.$refs.table.selectAll(false);
    },
    submitEdit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (!this.form.id) {
            this.$Message.warning("请先点击选择要修改的经理");
            return;
          }
          Vue.delete(this.form,'children');
          // //console.log(this.form);
          this.submitLoading = true;
          
          editJueSeManger(this.form).then(res => {
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
      this.$refs.formAdd.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          addJueSeManger(this.formAdd).then(res => {
            this.submitLoading = false;
            if (res.success) {
              this.$Message.success("添加成功");
              this.init();
              this.modalVisible = false;
            }
          });
        }
      });
    },
    tableAdd(v) {
      this.form = v;
      this.add();
    },
    add() {
      if (this.form.id == "" || this.form.id == null) {
        this.$Message.warning("请先点击选择一个经理");
        return;
      }
      this.modalTitle = "添加子经理";
      this.showParent = true;
      this.formAdd = {
        parentId: this.form.id,
        sortOrder: 0,
        status: 0
      };
      this.modalVisible = true;
    },
    addRoot() {
      this.modalTitle = "添加一级经理";
      this.showParent = false;
      this.formAdd = {
        parentId: 0,
        sortOrder: 0,
        status: 0
      };
      this.modalVisible = true;
    },
    changeSelect(v) {
      // console.log(v);
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
          deleteJueSeManger({ids: ids}).then(res => {
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
    }
  },
  mounted() {
    // 计算高度
    let height = document.documentElement.clientHeight;
    this.maxHeight = Number(height - 287) + "px";
    this.init();
  }
};
</script>