<template>
  <div>
    <span style="color:#ff0000">当前选择人员：{{userName}}</span>
    <!-- {{departmentNameData}} -->
    <card>
      <div>
        <Form ref="form" :model="form" :rules="formValidate" label-position="top">
          <Row :gutter="32">
            <Col span="24">
              <FormItem label="自我介绍">
                <Input v-model="introduce" type="textarea" :rows="20" placeholder="请在这里输入您的自我介绍，输入完成后单击保存即可，谢谢!" :readonly="isEdit" />
              </FormItem>
            </Col>
          </Row>
          <Row :gutter="32">
            <Col span="6">
              <Button type="primary" :loading="submitLoading" @click="submit">保存</Button>
              <Button :loading="submitLoading" @click="beginEdit">编辑</Button>
            </Col>
          </Row>
        </Form>
      </div>
    </card>
  </div>
</template>

<script>
import {
  updateIntroduce,
  getIntroduce,
  getDepartmentList,getUserByDepId,deleteDingUser,updateDingUser,getOneHua,addHua,editHuaOnDing,savequanXian} from "./index";
import addEditUser from './addEditUser';
// 编辑保安管理范围列表组件
export default {
  name: "anbao-fanwei",
  components: {
    addEditUser
  },
  data() {
    return {
      isEdit:true,
      departmentNameData:'未选择',
      form:{
        id:'未选择',
        introduce:'',
      },
      introduce:'',
      userName:'',
    }
  },
  methods: {
    init() {
      // this.getUserList();
    },
    beginEdit() {
      this.isEdit = false;
    },
    getUserList(){
      getIntroduce({id:this.departmentNameData}).then(res =>{
        this.introduce = res.result.introduce;
        this.userName = res.result.userName;
      })
    },
    submit() {
      updateIntroduce({id:this.departmentNameData,introduce:this.introduce}).then(res =>{
      })
      this.isEdit = true;
      this.$emit("on-submit", true);
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
