<template>
  <div>
    <Card>
      <div slot="title">
        <div class="edit-head">
          <a @click="close" class="back-title">
            <Icon type="ios-arrow-back" />返回
          </a>
          <div class="head-name">编辑</div>
          <span></span>
          <a @click="close" class="window-close">
            <Icon type="ios-close" size="31" class="ivu-icon-ios-close" />
          </a>
        </div>
      </div>
      <Form ref="form" :model="form" :label-width="100" :rules="formValidate" label-position="left" >        <FormItem label="宿舍门牌号" prop="houseNumber"  >
          <Input v-model="form.houseNumber" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="宿舍地址" prop="houseAddress"  >
          <Input v-model="form.houseAddress" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="房型" prop="houseType"  >
          <Select v-model="form.houseType" clearable style="width:570px">
            <Option
                  v-for="(item, i) in this.$store.state.dict.houseType"
                  :key="i"
                  :value="item.value"
                >{{item.title}}
            </Option>
          </Select>
        </FormItem>
        <FormItem label="配置" prop="houseType">
            <Select clearable style="width:570px" multiple :placeholder="form.houseSetting" @on-change="changeHouseSetting">
              <Option
                    v-for="(item, i) in this.$store.state.dict.houseSetting"
                    :key="i"
                    :value="item.value"
                  >{{item.title}}
              </Option>
            </Select>
          </FormItem>
        <FormItem label="面积" prop="areaMeasure">
          <Input v-model="form.areaMeasure" style="width:570px"/>
        </FormItem>
        <FormItem label="月租金" prop="rent">
          <Input v-model="form.rent" style="width:570px"/>
        </FormItem>
        <FormItem label="备注" prop="remarks">
          <Input v-model="form.remarks" clearable style="width:570px" />
        </FormItem>
        <Form-item class="br">
          <Button
            @click="handleSubmit"
            :loading="submitLoading"
            type="primary"
          >提交并保存</Button>
          <Button @click="handleReset">重置</Button>
          <Button type="dashed" @click="close">关闭</Button>
        </Form-item>
      </Form>
    </Card>
  </div>
</template>

<script>
// 根据你的实际请求api.js位置路径修改
import { editDormitory } from "./api.js";
export default {
  name: "edit",
  components: {
    },
  props: {
    data: Object
  },
  data() {
    return {
        submitLoading: false, // 表单提交状态
        form: { // 添加或编辑表单对象初始化数据
        houseNumber: "",
        houseAddress: "",
        houseType: "",
        houseSetting: "",
        areaMeasure: 0,
        rent: 0,
        remarks: "",
      },
      indeterminate: false,
      checkAll: false,
      checkAllGroup: [],
      // 表单验证规则
      formValidate: {
        houseNumber: [{ required: true, message: "不能为空", trigger: "blur" }],
        houseAddress: [{ required: true, message: "不能为空", trigger: "blur" }],
        houseType: [{ required: true, message: "不能为空", trigger: "blur" }],
        areaMeasure: [{ required: true, message: "不能为空", trigger: "blur" }],
        rent: [{ required: true, message: "不能为空", trigger: "blur" }],
      },
      isChangeHouseSetting:false,
    };
  },
  methods: {
    init() {
      this.handleReset();
      this.form = this.data;
    },
    // 房屋配置下拉框改动
    changeHouseSetting(e){
      this.isChangeHouseSetting = true;
      var array = e;
      this.form.houseSetting = "";
      for(var i = 0 ; i < array.length; i ++){
        if(i==0) this.form.houseSetting += array[i];
        else this.form.houseSetting += ',' + array[i];
      }
    },
    // 搜索栏重置
    handleReset() {
      this.$refs.form.resetFields();
    },
    // 编辑提交
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          editDormitory(this.form).then(res => {
            this.submitLoading = false;
            if (res.success) {
              this.$Message.success("操作成功");
              this.$emit("submited", true);
            }
          });
        }
      });
    },
    close() {
      this.$emit("close", true);
    },
  },
  mounted() {
    this.init();
  }
};
</script>
<style lang="less">
.edit-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    position: relative;

    .back-title {
        color: #515a6e;
        display: flex;
        align-items: center;
    }

    .head-name {
        display: inline-block;
        height: 20px;
        line-height: 20px;
        font-size: 16px;
        color: #17233d;
        font-weight: 500;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .window-close {
        z-index: 1;
        font-size: 12px;
        position: absolute;
        right: 0px;
        top: -5px;
        overflow: hidden;
        cursor: pointer;

        .ivu-icon-ios-close {
            color: #999;
            transition: color .2s ease;
        }
    }

    .window-close .ivu-icon-ios-close:hover {
        color: #444;
    }
}
</style>