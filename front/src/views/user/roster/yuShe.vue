<template>
  <div>
    <!-- <span style="color:#ff0000">当前选择人员：{{form.username}}</span> -->
    <!-- {{departmentNameData}} -->
    <card>
      <div>
        <Form ref="form" :model="form" :rules="formValidate" label-position="top">
          <Row :gutter="32">
            <Col span="24">
              <FormItem label="角色分配" prop="roleIds">
                <Select multiple v-model="form.roleIds" @on-change="changeRoles" :disabled="isEdit">
                  <Option
                    v-for="item in roleList"
                    :value="item"
                    :key="item.id"
                    :label="item.title"
                  >
                    <span style="margin-right:10px;">{{ item.title }}</span>
                    <span style="color:#ccc;">{{ item.requirement }}</span>
                  </Option>
                </Select>
              </FormItem>
            </Col>
          </Row>
          <Row :gutter="32">
            <Col span="24">
              <FormItem label="权限详情">
                <Input v-model="form.permissionPreset" type="textarea" :rows="6" placeholder="请在上方选择赋予此人的预分配权限!" readonly/>
              </FormItem>
            </Col>
          </Row>
          <Row :gutter="32">
            <Col span="24">
              <FormItem label="额外备注">
                <Input v-model="zidingyi" type="textarea" :rows="4" placeholder="如果需要额外说明，请填写此栏目!" :readonly="isEdit"/>
              </FormItem>
            </Col>
          </Row>
          <Row :gutter="32">
            <Col span="24">
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
import { getRoleList,getAllRoleList} from "@/api/index";
import {getOneHua,savePermissionPreset} from "./index";
export default {
  name: "anbao-fanwei",
  components: {
  },
  data() {
    return {
      isEdit:true,
      changeBirthday:false,
      birthdayTemp: '请选择生日',
      departmentNameData:'未选择',
      form:{
        id:'未选择',
        permissionPreset:'',
      },
      roleList: [],
      oaroles:'',
      roleList1: [],
      elseroles:'',
      zidingyi:'',
      roleList: []
    }
  },
  methods: {
    init() {
      this.getRoleList();
    },
    getRoleList() {
      getAllRoleList().then(res => {
        if (res.success) {
          this.roleList = res.result;
        }
      });
    },
    changeRoles(e){
      console.log(e);
      var str = "OA权限组：\n";
      for(var i = 0 ; i < e.length ; i ++){
        str += e[i].title + '(' + e[i].requirement + ')\n';
      }
      this.form.permissionPreset = str;
    },
    getRoleList() {
      var that = this;
      getAllRoleList().then(res => {
        if (res.success) {
          that.roleList = res.result;
        }
      });
    },
    beginEdit() {
      this.isEdit = false;
    },
    getUserList(){
      getOneHua({id:this.departmentNameData}).then(res =>{
        this.form = res.result;
      })
    },
    submit() {
      this.form.permissionPreset =  this.form.permissionPreset  + '\n自定义备注: ' +  this.zidingyi;
      savePermissionPreset(this.form).then(res =>{
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
