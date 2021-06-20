<template>
  <div class="search">
    <add v-if="currView=='add'" @close="currView='index'" @submited="submited" />
    <edit v-if="currView=='edit'" @close="currView='index'" @submited="submited" :data="formData" />
    <Card v-show="currView=='index'">
      <!-- {{shengshiqu}} -->
        <Row @keydown.enter.native="handleSearch">
          <Form ref="searchForm" :model="searchForm" inline :label-width="70" class="search-form">
            <Form-item label="是否封存" prop="ban">
              <Select v-model="searchForm.ban" placeholder="请选择" clearable style="width: 200px">
                  <Option
                    v-for="(item, i) in this.$store.state.dict.ban"
                    :key="i"
                    :value="item.value"
                  >{{item.title}}</Option>
                </Select>
            </Form-item>
            <Form-item label="客户姓名" prop="name">
              <Input type="text" v-model="searchForm.name" placeholder="请输入客户姓名" clearable style="width: 200px"/>
            </Form-item>
            <!-- <Form-item label="地区">
              <al-cascader @on-change="changeShengShiQu" style="width: 200px" />
            </Form-item> -->
            <span v-if="drop">
              <Form-item label="客户分类" prop="huType">
              <Input type="text" v-model="searchForm.huType" placeholder="请输入客户分类" clearable style="width: 200px"/>
            </Form-item>
              <Form-item label="完整地区" prop="wanZheng">
                <Cascader :data="cityData" @on-change="changeCityData" v-model="cityDataAns" style="width:300px"></Cascader> 
              </Form-item>
              <Form-item label="分公司" prop="fenSi">
                <Input type="text" v-model="searchForm.fenSi" placeholder="请输入分公司" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="大区" prop="bigQu">
                <Input type="text" v-model="searchForm.bigQu" placeholder="请输入大区" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="区域" prop="areaNumber">
                <Input type="text" v-model="searchForm.areaNumber" placeholder="请输入区域编码" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="省份" prop="sheng">
                <Input type="text" v-model="searchForm.sheng" placeholder="请输入省份" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="地级市" prop="shi">
                <Input type="text" v-model="searchForm.shi" placeholder="请输入地级市" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="区县" prop="peopleForm">
                <Input type="text" v-model="searchForm.peopleForm" placeholder="请输入市县镇区" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="客商编码" prop="shangNumber">
                <Input type="text" v-model="searchForm.shangNumber" placeholder="请输入客商编码" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="客商名称" prop="shangName">
                <Input type="text" v-model="searchForm.shangName" placeholder="请输入客商名称" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="客户来源" prop="huForm">
                <Input type="text" v-model="searchForm.huForm" placeholder="请输入客户来源" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="客商类型" prop="shangForm">
                <Input type="text" v-model="searchForm.shangForm" placeholder="请输入客商类型" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="信用额度" prop="xinMoney">
                <Input type="text" v-model="searchForm.xinMoney" placeholder="请输入信用额度" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="客户等级" prop="huLevel">
                <Input type="text" v-model="searchForm.huLevel" placeholder="请输入客户等级" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="客户质量" prop="huLiang">
                <Input type="text" v-model="searchForm.huLiang" placeholder="请输入客户质量" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="城市类型" prop="cityType">
                <Select v-model="searchForm.cityType" placeholder="请选择城市类型" clearable style="width: 200px">
                  <Option
                    v-for="(item) in this.cityTypeList"
                    :key="item.title"
                    :value="item.title"
                  >{{item.title}}</Option>
                </Select>
                <!-- <Input type="text" v-model="searchForm.cityType" placeholder="请输入城市类型" clearable style="width: 200px"/> -->
              </Form-item>
              <Form-item label="城市等级" prop="cityLevel">
                <Select v-model="searchForm.cityLevel" placeholder="请选择城市等级" clearable style="width: 200px">
                  <Option
                    v-for="(item) in this.cityLevelList"
                    :key="item.title"
                    :value="item.title"
                  >{{item.title}}</Option>
                </Select>
                <!-- <Input type="text" v-model="searchForm.cityLevel" placeholder="请输入城市等级" clearable style="width: 200px"/> -->
              </Form-item>
              <!-- @on-change="changeCountyType" -->
              <Form-item label="县级类型" prop="xianType">
                <Select placeholder="请选择县级类型" clearable style="width: 200px" v-model="searchForm.xianType" >
                  <Option
                    v-for="(item) in this.countyTypeList"
                    :key="item.title"
                    :value="item.title"
                  >{{item.title}}</Option>
                </Select>
                <!-- <Input type="text" v-model="searchForm.xianType" placeholder="请输入县级类型" clearable style="width: 200px"/> -->
              </Form-item>
              <Form-item label="店面密度" prop="dianMi">
                <Input type="text" v-model="searchForm.dianMi" placeholder="请输入店面密度" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="单店销量" prop="sellNumber">
                <Input type="text" v-model="searchForm.sellNumber" placeholder="请输入单店销量" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="区域经理" prop="areaManger">
                <Input type="text" v-model="searchForm.areaManger" placeholder="请输入区域经理" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="业务员" prop="yeWu">
                <Input type="text" v-model="searchForm.yeWu" placeholder="请输入业务员" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="客服" prop="keFu">
                <Input type="text" v-model="searchForm.keFu" placeholder="请输入客服" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="联系人" prop="callPeople">
                <Input type="text" v-model="searchForm.callPeople" placeholder="请输入联系人" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="街道" prop="jieDao">
                <Input type="text" v-model="searchForm.jieDao" placeholder="请输入街道" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="建材市场" prop="jianCai">
                <Input type="text" v-model="searchForm.jianCai" placeholder="请输入建材市场" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="备注" prop="remark">
                <Input type="text" v-model="searchForm.remark" placeholder="请输入备注" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="开户行" prop="kaiHang">
                <Input type="text" v-model="searchForm.kaiHang" placeholder="请输入开户行" clearable style="width: 200px"/>
              </Form-item>
              <Form-item label="银行账户" prop="yinHang">
                <Input type="text" v-model="searchForm.yinHang" placeholder="请输入银行账户" clearable style="width: 200px"/>
              </Form-item>
            </span>
            <Form-item style="margin-left:-35px;" class="br">
              <Button @click="handleSearch" type="primary" icon="ios-search">搜索</Button>
              <Button type="warning" @click="handleReset">重置</Button>
              <a class="drop-down" @click="dropDown">
                {{dropDownContent}}
                <Icon :type="dropDownIcon"></Icon>
              </a>
            </Form-item>
          </Form>
        </Row>
      <Row class="operation">
        <Button @click="add" type="info" icon="md-add">添加</Button>
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
  </div>
</template>

<script>
// 根据你的实际请求api.js位置路径修改
import { getKeHuRosterList, deleteKeHuRoster,findAllCityLevel,findAllCityType ,getAllCountyType,getAllShengShiXian,getXianByName,getShinByName} from "./api.js";
// 根据你的实际添加编辑组件位置路径修改
import add from "./add.vue";
import edit from "./edit.vue";
export default {
  name: "single-window",
  components: {
    add,
    edit
  },
  data() {
    return {
      shengshiqu:'',
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
        title: "是否封存",
        key: "ban",
        minWidth: 120,
        sortable: false,
      },
      {
        title: "客户姓名",
        key: "name",
        minWidth: 80,
        sortable: false,
      },
      {
        title: "客户分类",
        key: "huType",
        minWidth: 120,
        sortable: false,
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
        title: "客户来源",
        key: "huForm",
        minWidth: 120,
        sortable: false,
      },
      {
        title: "客商类型",
        key: "shangForm",
        minWidth: 120,
        sortable: false,
      },
      {
        title: "客户等级",
        key: "huLevel",
        minWidth: 120,
        sortable: false,
      },
      {
        title: "客户质量",
        key: "huLiang",
        minWidth: 120,
        sortable: false,
      },
      {
        title: "操作",
        key: "action",
        align: "center",
        width: 200,
        render: (h, params) => {
          return h("div", [
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
      total: 0 ,// 表单数据总数
      cityData:[],
      cityDataAns:'',
    };
  },
  methods: {
    init() {
      this.getDataList();
      this.findAllCityLevelFx();
      this.findAllCityTypeFx();
      this.getAllCountyTypeFx();
      this.getAllShengShiXianFx();
    },
    getAllShengShiXianFx(){
      var that = this;
      getAllShengShiXian().then(res => {
        console.log(res);
        that.cityData = res.result;
      })
    },
    changeCityData(e1,e2){
      var that = this;
      // console.log(e1); // 简略数组
      // console.log(e2); // 完整数组
      this.searchForm.fenSi = e1[0]; 
      this.searchForm.bigQu = e1[1];
      this.searchForm.areaNumber = e1[2];
      this.searchForm.sheng = e1[3];
      this.searchForm.shi = e1[4];
      this.searchForm.peopleForm = e1[5];
      // 请求城市类型 城市等级
      getShinByName({sheng:e1[3],shi:e1[4]}).then(res => {
        // console.log(res);
        that.form.cityType = res.result[0].cityType;
        that.form.cityLevel = res.result[0].cityLevel;
      });
      // 请求县级类型 店面密度 单店销量
      getXianByName({shi:e1[4],qu:e1[5]}).then(res => {
        // console.log(res);
        that.form.xianType = res.result[0].countyType;
        that.form.dianMi = res.result[0].miDu;
        that.form.sellNumber = res.result[0].sellNumber;
      });
    },
    // changeCountyType(e){
    //   this.searchForm.xianType = e.title;
    // },
    getAllCountyTypeFx(){
      var that =this;
      getAllCountyType().then(res => {
        that.countyTypeList = res.result;
      })
    },
    findAllCityTypeFx(){
      var that = this;
      findAllCityType().then(res => {
        that.cityTypeList = res.result;
      })
    },
    findAllCityLevelFx(){
      var that = this;
      findAllCityLevel().then(res => {
        that.cityLevelList = res.result;
      })
    },
    changeShengShiQu(e){
      // console.log(e);
      this.searchForm.sheng = e[0].code;
      this.searchForm.shi = e[1].code;
      this.searchForm.peopleForm = e[2].code;
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
        getKeHuRosterList(this.searchForm).then(res => {
          this.loading = false;
          if (res.success) {
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
            deleteKeHuRoster({ids: v.id}).then(res => {
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
            deleteKeHuRoster({ids: ids}).then(res => {
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