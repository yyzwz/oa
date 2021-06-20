<template>
  <div>
    <!-- <span style="color:#ff0000">当前选择人员：{{form.username}}</span> -->
    <!-- {{departmentNameData}} -->
    <card>
      <div>
        <Form ref="searchForm" :model="searchForm" inline :label-width="70">
            <Form-item label="选择类型">
              <Select v-model="searchForm.hou" clearable style="width: 200px">
                  <Option
                    v-for="(item, i) in this.findAllHouList"
                    :key="i"
                    :value="item"
                  >{{item}}</Option>
                </Select>
            </Form-item>
          <Form-item style="margin-left:-35px;" class="br">
            <Button @click="f5" type="primary" icon="ios-search">搜索</Button>
            <Button @click="handleReset">重置</Button>
          </Form-item>
        </Form>
        <Button type="error" @click="ShowAddFuJian">上传模板</Button>
        <Button type="info" @click="f5">刷新</Button>
        <Row>
        <Table
          :loading="loading"
          border
          :columns="columns"
          :data="fileData"
          sortable="custom"
          @on-sort-change="changeSort"
          @on-selection-change="showSelect"
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
          :page-size-opts="[10,20,50]"
          size="small"
          show-total
          show-elevator
          show-sizer
        ></Page>
      </Row>
      </div>
    </card>
    <Modal
        v-model="isShowAddFuJian"
        title="新增模板"
        @on-ok="updateFuJian"
        @on-cancel="updateFuJiancancel">
        <span>请输入要上传的模板名称</span>
        <input v-model="addFuJianNameTemp" />
    </Modal>

  </div>
</template>

<script>
import  Cookies  from  "js-cookie";
import {findAllHou,getDepartmentList,getUserByDepId,getFuJianData,getOneHuaGang,addFuJianMoBan,deleteFuJianMoBan,getFuJianAllData} from "./index";
export default {
  name: "fujian",
  components: {
  },
  data() {
    return {
      isEdit:true,
      form:{
        id:'未选择',
      },
      fujianform:{
        id:'',
        name:'',
      },
      searchForm: {
        hou:'',
        pageNumber: 1,
        pageSize: 10,
        sort: "createTime",
        order: "desc"
      },
      findAllHouList:[],
      addFuJianNameTemp:'',
      isShowAddFuJian: false,
      fileData:[],
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
          title: "资料名称",
          key: "dataName",
          minWidth: 100,
          align: "center",
          sortable: true,
        },
        {
          title: "扩展名",
          key: "hou",
          minWidth: 60,
          align: "center",
          sortable: true,
        },
        {
          title: "下载次数",
          key: "times",
          minWidth: 80,
          align: "center",
          sortable: true,
        },
        {
          title: "上传人",
          key: "createBy",
          minWidth: 60,
          align: "center",
          sortable: true,
        },
        {
          title: "下载",
          key: "isHave",
          minWidth: 100,
          sortable: true,
          render: (h, params) => {
            var that = this;
            let fujianid = params.row.id;
            if(params.row.isHave == 1){
              return h("div", [
                h(
                  "Button",
                  {
                    props: {
                      type: "info",
                      size: "small"
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        window.open("http://127.0.0.1:7778/xboot/documentTemplate/downloadFile/" + fujianid,"_blank");
                        this.f5();
                      }
                    }
                  },
                  "下载"
                ),
                h(
                  "Button",
                  {
                    props: {
                      type: "error",
                      size: "small"
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        this.deteleFuJian(fujianid);
                      }
                    }
                  },
                  "删除"
                )
              ])
            }else{
              return h("div", [
                h(
                  "Upload",
                  {
                    props: {
                      type: "drag",
                      icon: "ios-cloud-upload-outline",
                      action:'/xboot/documentTemplate/file/' + fujianid,
                      data:{

                      },
                      'on-success':that.uploadFuJianSuccess,
                      'on-error':that.uploadFuJianError,
                      'before-upload':that.uploadFuJianBefore,
                      'on-progress':that.uploadFuJianProgress,
                    },
                  },
                  "上传"
                )
              ])
            }
          }
        },
        {
          title: "创建时间",
          key: "createTime",
          minWidth: 100,
          align: "center",
          sortable: true,
        },
      ],
    }
  },
  methods: {
    init() {
      this.f5();
      this.findAllHou();
    },
    findAllHou(){
      findAllHou().then(res =>{
        this.findAllHouList = res.result;
        // console.log(res);
      })
    },
    getFuJianData(){
      getFuJianAllData(this.searchForm).then(res =>{
        // //console.log("getFuJianData");
        console.log(res);
        this.fileData = res.result.records;
      })
    },
    handleReset() {
      this.searchForm.hou = "";
      this.searchForm.pageNumber = 1;
      this.searchForm.pageSize = 10;
      this.f5();
    },
    updateFuJian(){
      this.fujianform.huaId = this.form.id;
      this.fujianform.huaName = this.form.username;
      this.fujianform.dataName = this.addFuJianNameTemp;
      this.fujianform.isHave = 0;
      addFuJianMoBan(this.fujianform).then(res =>{
        this.f5();
      });
      this.f5();
      this.isShowAddFuJian = false;
    },
    updateFuJiancancel(){
      this.isShowAddFuJian = false;
    },
    ShowAddFuJian(){
      this.isShowAddFuJian = true;
    },
    uploadFuJianError(res,file){

    },
    uploadFuJianSuccess(){
      //console.log("uploadFuJianSuccess");
      this.f5();
    },
    f5(){
      this.getFuJianData();
    },
    deteleFuJian(fujianid){
      this.$Modal.confirm({
        title: "确认删除",
        content: "您确认要删除该模板吗?",
        loading: true,
        onOk: () => {
          deleteFuJianMoBan({ids: fujianid}).then(res => {
            this.$Modal.remove();
            if (res.success) {
              this.$Message.success("删除成功");
              this.f5();
            }
          });
        }
      });
    },
  },
  mounted() {
    this.init();
  }
}
</script>
<style>
</style>
