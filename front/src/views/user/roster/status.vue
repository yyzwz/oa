<template>
  <div>
    <!-- <span style="color:#ff0000">当前选择人员：{{form.userName}}</span> -->
    <!-- {{departmentNameData}} -->
    <card>
      <div v-if="form.status==-2">
          <span style="color:#ff0000;font-size:26px;">当前尚未选择人员</span>
      </div>
      <div v-else>
         <span style="color:#ff0000;font-size:26px;"> 当前选择人员： {{form.userName}}</span>
      </div>
    </card>
    <card>
      <div v-if="form.status==-2">
          <span style="color:#ff0000;font-size:26px;">工号：当前尚未选择人员</span>
      </div>
      <div v-else>
         <span style="color:#ff0000;font-size:26px;"> 工号：{{form.jobNumber}}</span>
      </div>
    </card>
    <card>
      <div v-if="form.status==-2">
          <span style="color:#ff0000;font-size:26px;">手机号：当前尚未选择人员</span>
      </div>
      <div v-else>
         <span style="color:#ff0000;font-size:26px;"> 手机号：{{form.mobile}}</span>
      </div>
    </card>
    <card>
      <div v-if="form.status==-2">
          当前人员状态：未选择用户
      </div>
      <div v-else-if="form.status==0">
          <span style="color:#00BFFF;font-size:26px;">当前人员状态：正常启用</span>
      </div>
      <div v-else>
         <span style="color:#ff0000;font-size:26px;"> 当前人员状态： 已停职</span>
      </div>
    </card>

    <card>
      <div v-if="form.status==-2">
        <span style="color:#ff0000;font-size:26px;">离复职操作：未选择用户</span>
      </div>
      <div v-else-if="form.status==0">
        <span style="color:#ff0000;font-size:26px;">离复职操作：</span>
        <Button @click="lizhi" type="error" :size="large">离职</Button>
      </div>
      <div v-else>
        <span style="color:#ff0000;font-size:26px;">离复职操作：</span>
        <Button @click="fuzhi" type="info" :size="large">复职</Button>
      </div>
    </card>
    
    <card>
      
      <div v-if="form.status==-2">
          <span style="color:#ff0000;font-size:26px;">未选择用户</span>
      </div>
      <div v-else>
        <span style="color:#ff0000;font-size:26px;">永久删除：</span>
        <Button @click="deleteOneRoster" type="error">删除花名册</Button>
      </div>
    </card>
  </div>
</template>

<script>
import {
  deleteRoster,
  getDepartmentList,
  getUserByDepId,
  deleteDingUser,
  updateDingUser,
  getOneHua,
  tui,
  fu
  } from "./index";
import addEditUser from './addEditUser';
// 编辑保安管理范围列表组件
export default {
  name: "anbao-fanwei",
  components: {
    addEditUser
  },
  data() {
    return {
      departmentNameData:'未选择',
      form:{
        status:-2,
      }
    }
  },
  methods: {
    init() {
      // this.getUserList();
    },
    getUserList(){
      getOneHua({id:this.departmentNameData}).then(res =>{
        this.form = res.result;
        // console.log(res);
        if(this.form.status == undefined){
          this.form.status = -2;
        }
      })
    },
    deleteOneRoster(){
      console.log("deleteRoster");
      this.$Modal.confirm({
        title: "确认永久删除该员工?",
        content: "该操作会删除所有关联的信息,如部门、权限、附件",
        loading: true,
        onOk: () => { 
          deleteRoster({id: this.departmentNameData}).then(res =>{
            this.$Message.success("删除成功");
            this.$Modal.remove();
            this.$emit("on-submit", true);
          })
        }
      });
    },
    lizhi(){
      this.$Modal.confirm({
        title: "确认辞退",
        content: "您确认要辞退此员工?",
        loading: true,
        onOk: () => { 
          tui({id: this.departmentNameData}).then(res => {
            this.$Modal.remove();
            if (res.success) {
              this.$Message.success("辞退成功");
              this.getUserList();
            }
          });
        }
      });
    },
    fuzhi(){
      this.$Modal.confirm({
        title: "确认复职",
        content: "您确认要复职此员工?",
        loading: true,
        onOk: () => { 
          fu({id: this.departmentNameData}).then(res => {
            this.$Modal.remove();
            if (res.success) {
              this.$Message.success("复职成功");
              this.getUserList();
            }
          });
        }
      });
    }
  },
  mounted() {
    this.init();
  },
  props: ['departmentName'],
  watch:{
      departmentName:function(newVal,oldVal){
        this.departmentNameData = newVal;
        this.getUserList();
      }
  },
   
}
</script>

<style>

</style>
