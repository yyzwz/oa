<template>
  <div class="search">
    <add v-if="currView=='add'" @close="currView='index'" @submited="submited" />
    <edit v-if="currView=='edit'" @close="currView='index'" @submited="submited" :data="formData" />
    <Card v-show="currView=='index'">
        <Row @keydown.enter.native="handleSearch">
          <Form ref="searchForm" :model="searchForm" inline :label-width="70" class="search-form">
            <Form-item label="客商编码" prop="shangNumber">
              <Input type="text" v-model="searchForm.shangNumber" placeholder="请输入客商编码" clearable style="width: 200px"/>
            </Form-item>
            <Form-item label="客商名称" prop="shangName">
              <Input type="text" v-model="searchForm.shangName" placeholder="请输入客商名称" clearable style="width: 200px"/>
            </Form-item>
            <span v-if="drop">
              <Form-item label="客商类型" prop="shangType">
                <Input type="text" v-model="searchForm.shangType" placeholder="请输入客商类型" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="区域" prop="area">
                <Input type="text" v-model="searchForm.area" placeholder="请输入区域" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="省份" prop="sheng">
                <Input type="text" v-model="searchForm.sheng" placeholder="请输入省份" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="地级市" prop="city">
                <Input type="text" v-model="searchForm.city" placeholder="请输入地级市" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="县" prop="xian">
                <Input type="text" v-model="searchForm.xian" placeholder="请输入县" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="街道" prop="jieDao">
                <Input type="text" v-model="searchForm.jieDao" placeholder="请输入街道" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="具体地址" prop="address">
                <Input type="text" v-model="searchForm.address" placeholder="请输入具体地址" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="开户行" prop="openHang">
                <Input type="text" v-model="searchForm.openHang" placeholder="请输入开户行" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="银行账户" prop="hangNumber">
                <Input type="text" v-model="searchForm.hangNumber" placeholder="请输入银行账户" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="信用额度" prop="xinMoney">
                <Input type="text" v-model="searchForm.xinMoney" placeholder="请输入信用额度" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="是否封存" prop="ban">
                <Select v-model="searchForm.ban" placeholder="请选择" clearable style="width: 200px">
                  <Option
                    v-for="(item, i) in this.$store.state.dict.ban"
                    :key="i"
                    :value="item.value"
                  >{{item.title}}</Option>
                </Select>
              </Form-item>
              <Form-item label="备注" prop="remark">
                <Input type="text" v-model="searchForm.remark" placeholder="请输入备注" clearable style="width: 200px"/>
              </Form-item>
            </span>
            <Form-item style="margin-left:-35px;" class="br">
              <Button @click="handleSearch" type="success" icon="ios-search">搜索</Button>
              <Button @click="handleReset" type="warning">重置</Button>
              <a class="drop-down" @click="dropDown">
                {{dropDownContent}}
                <Icon :type="dropDownIcon"></Icon>
              </a>
            </Form-item>
          </Form>
        </Row>
      <Row class="operation">
        <Button type="info" @click="add" icon="md-add">添加</Button>
        <Button type="error" @click="delAll" icon="md-trash">批量删除</Button>
        <Button type="success" @click="getDataList" icon="md-refresh">刷新</Button>
        <Button type="dashed" @click="openTip=!openTip">{{openTip ? "关闭提示" : "开启提示"}}</Button>
      </Row>
      <Row v-show="openTip">
        <Alert show-icon>
          已选择
          <span class="select-count">{{selectCount}}</span> 项
          <a class="select-clear" @click="clearSelectAll">清空</a>
        </Alert>
      </Row>
      <Row>
        <Table
          :loading="loading"
          border
          :columns="columns"
          :data="data"
          ref="table"
          sortable="custom"
          @on-sort-change="changeSort"
          @on-selection-change="changeSelect"
        ></Table>
      </Row>
      <Row type="flex" justify="end" class="page">
        <Page
          :current="searchForm.pageNumber"
          :total="total"
          :page-size="searchForm.pageSize"
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
    <look v-show="isShowLookTitle" :shangId="keShangId" :keShangName="keShangName" @on-submit="getDataList"></look>
  </div>
</template>

<script>
// 根据你的实际请求api.js位置路径修改
import { getKeShangRosterList, deleteKeShangRoster } from "./api.js";
// 根据你的实际添加编辑组件位置路径修改
import add from "./add.vue";
import edit from "./edit.vue";
import look from "./look.vue";
export default {
  name: "single-window",
  components: {
    add,
    edit,
    look
  },
  data() {
    return {
      isShowLookTitle:false,
      openSearch: true, // 显示搜索
      openTip: true, // 显示提示
      formData: {},
      currView: "index",
      loading: true, // 表单加载状态
      drop: false,
      dropDownContent: "展开",
      dropDownIcon: "ios-arrow-down",
      searchForm: { // 搜索框初始化对象
        pageNumber: 1, // 当前页数
        pageSize: 10, // 页面大小
        sort: "createTime", // 默认排序字段
        order: "desc", // 默认排序方式
      },
      selectList: [], // 多选数据
      selectCount: 0, // 多选计数
      columns: [
      // 表头
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
        title: "客商编码",
        key: "shangNumber",
        minWidth: 120,
        sortable: false,
      },
      {
        title: "客商名称",
        key: "shangName",
        minWidth: 120,
        sortable: false,
      },
      {
        title: "客商类型",
        key: "shangType",
        minWidth: 120,
        sortable: false,
      },
      {
        title: "省份",
        key: "sheng",
        minWidth: 120,
        sortable: false,
      },
      {
        title: "地级市",
        key: "city",
        minWidth: 120,
        sortable: false,
      },
      {
        title: "县",
        key: "xian",
        minWidth: 120,
        sortable: false,
      },
      {
        title: "是否封存",
        key: "banTemp",
        minWidth: 120,
        sortable: false,
      },
      {
        title: "操作",
        key: "action",
        align: "center",
        minWidth: 300,
        render: (h, params) => {
          return h("div", [
             h(
              "Button",
              {
                props: {
                  type: "warning",
                  size: "small",
                  icon: "ios-create-outline"
                },
                style: {
                  marginRight: "5px"
                },
                on: {
                  click: () => {
                    this.lookUser(params.row);
                  }
                }
              },
              "查看用户"
            ),
            h(
              "Button",
              {
                props: {
                  type: "primary",
                  size: "small",
                  icon: "ios-create-outline"
                },
                style: {
                  marginRight: "5px"
                },
                on: {
                  click: () => {
                    this.edit(params.row);
                  }
                }
              },
              "编辑"
            ),
            h(
              "Button",
              {
                props: {
                  type: "error",
                  size: "small",
                  icon: "md-trash"
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
    ],
      data: [], // 表单数据
      pageNumber: 1, // 当前页数
      pageSize: 10, // 页面大小
      total: 0, // 表单数据总数
      keShangId : '',
      keShangName: '',
    };
  },
  methods: {
    init() {
      this.getDataList();
    },
    lookUser(e){
      console.log(e);
      this.isShowLookTitle = true;
      this.keShangId = e.id;
      this.keShangName = e.shangName;
    },
    submited() {
      this.currView = "index";
      this.getDataList();
    },
    changePage(v) {
      this.searchForm.pageNumber = v;
      this.getDataList();
      this.clearSelectAll();
    },
    changePageSize(v) {
      this.searchForm.pageSize = v;
      this.getDataList();
    },
    handleSearch() {
      this.searchForm.pageNumber = 1;
      this.searchForm.pageSize = 10;
      this.getDataList();
    },
    handleReset() {
      this.$refs.searchForm.resetFields();
      this.searchForm.pageNumber = 1;
      this.searchForm.pageSize = 10;
      // 重新加载数据
      this.getDataList();
    },
    changeSort(e) {
      this.searchForm.sort = e.key;
      this.searchForm.order = e.order;
      if (e.order === "normal") {
        this.searchForm.order = "";
      }
      this.getDataList();
    },
    dropDown() {
      if (this.drop) {
        this.dropDownContent = "展开";
        this.dropDownIcon = "ios-arrow-down";
      } else {
        this.dropDownContent = "收起";
        this.dropDownIcon = "ios-arrow-up";
      }
      this.drop = !this.drop;
    },
    clearSelectAll() {
      this.$refs.table.selectAll(false);
    },
    changeSelect(e) {
      this.selectList = e;
      this.selectCount = e.length;
    },
    getDataList() {
      this.loading = true;
        getKeShangRosterList(this.searchForm).then(res => {
          this.loading = false;
          if (res.success) {
            for(var i = 0 ; i < res.result.records.length ; i ++){
              if(res.result.records[i].ban == "正常"){
                res.result.records[i].banTemp = "正常";
                res.result.records[i].ban = false;
              }else{
                res.result.records[i].banTemp = "封存";
                res.result.records[i].ban = true;
              }
            }
            this.data = res.result.records;
            this.total = res.result.total;
          }
        });
    },
    add() {
      this.currView = "add";
    },
    edit(v) {
      // 转换null为""
      for (let attr in v) {
        if (v[attr] == null) {
          v[attr] = "";
        }
      }
      let str = JSON.stringify(v);
      let data = JSON.parse(str);
      this.formData = data;
      this.currView = "edit";
    },
    remove(v) {
      this.$Modal.confirm({
        title: "确认删除",
        // 记得确认修改此处
        content: "您确认要删除 " + v.name + " ?",
        loading: true,
        onOk: () => {
          // 删除
            deleteKeShangRoster({ids: v.id}).then(res => {
              this.$Modal.remove();
              if (res.success) {
                this.$Message.success("操作成功");
                this.getDataList();
              }
            });
        }
      });
    },
    delAll() {
      if (this.selectCount <= 0) {
        this.$Message.warning("您还未选择要删除的数据");
        return;
      }
      this.$Modal.confirm({
        title: "确认删除",
        content: "您确认要删除所选的 " + this.selectCount + " 条数据?",
        loading: true,
        onOk: () => {
          let ids = "";
          this.selectList.forEach(function(e) {
            ids += e.id + ",";
          });
          ids = ids.substring(0, ids.length - 1);
          // 批量删除
            deleteKeShangRoster({ids: ids}).then(res => {
              this.$Modal.remove();
              if (res.success) {
                this.$Message.success("操作成功");
                this.clearSelectAll();
                this.getDataList();
              }
            });
        }
      });
    }
  },
  mounted() {
    this.init();
  }
};
</script>
<style lang="less">
// 建议引入通用样式 具体路径自行修改 可删除下面样式代码
// @import "../../../styles/table-common.less";
.search {
    .operation {
        margin-bottom: 2vh;
    }
    .select-count {
        font-weight: 600;
        color: #40a9ff;
    }
    .select-clear {
        margin-left: 10px;
    }
    .page {
        margin-top: 2vh;
    }
    .drop-down {
        margin-left: 5px;
    }
}
</style>