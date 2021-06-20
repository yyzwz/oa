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
      <Form ref="form" :model="form" :label-width="100" :rules="formValidate" label-position="left" >  
        <!-- {{cityDataAns}} -->
        <FormItem label="客户姓名" prop="name"  >
            <Input v-model="form.name" clearable style="width:570px"/>
          </FormItem>
        <FormItem label="所在地" prop="ban"  >
            <Cascader :placeholder="cityTemp" :data="cityData" @on-change="changeCityData" v-model="cityDataAns"></Cascader>  
        </FormItem>  
        <FormItem label="城市类型" prop="cityType"  >
            <Input v-model="form.cityType" readonly clearable style="width:570px"/>
          </FormItem>
          <FormItem label="城市等级" prop="cityLevel"  >
            <Input v-model="form.cityLevel" readonly clearable style="width:570px"/>
          </FormItem>
          <FormItem label="县级类型" prop="xianType"  >
            <Input v-model="form.xianType" readonly clearable style="width:570px"/>
          </FormItem>
          <FormItem label="店面密度" prop="dianMi"  >
            <Input v-model="form.dianMi" readonly clearable style="width:570px"/>
          </FormItem>
          <FormItem label="单店销量" prop="sellNumber"  >
            <Input v-model="form.sellNumber" readonly clearable style="width:570px"/>
          </FormItem>
          <FormItem label="是否封存" prop="ban"  >
            <i-switch v-model="form.ban" readonly true-value="封存" false-value="正常" ></i-switch>
          </FormItem>
          <!-- <Form-item label="地区">
            <al-cascader @on-change="changeShengShiQu" style="width: 200px" />
          </Form-item> -->
          <FormItem label="客商类型" prop="shangForm"  >
            <Select :placeholder="form.shangForm" clearable style="width: 200px" @on-change="changeShangType">
              <Option
                v-for="(item) in this.keShangTypeList"
                :key="item.title"
                :value="item"
              >{{item.title}}</Option>
            </Select>
            <!-- <Input v-model="form.shangForm" clearable style="width:570px"/> -->
          </FormItem>
          <FormItem label="客户分类" prop="huType"  >
            <Input v-model="form.huType" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="客商编码" prop="shangNumber"  >
            <Input v-model="form.shangNumber" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="客商名称" prop="shangName"  >
            <Input v-model="form.shangName" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="客户来源" prop="huForm"  >
            <Input v-model="form.huForm" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="信用额度" prop="xinMoney"  >
            <Input v-model="form.xinMoney" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="客户等级" prop="huLevel"  >
            <Input v-model="form.huLevel" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="客户质量" prop="huLiang"  >
            <Input v-model="form.huLiang" clearable style="width:570px"/>
          </FormItem>
          <!-- <FormItem label="分公司" prop="fenSi"  >
            <Input v-model="form.fenSi" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="大区" prop="bigQu"  >
            <Input v-model="form.bigQu" clearable style="width:570px"/>
          </FormItem> -->
          <!-- <FormItem label="区域编码" prop="areaNumber"  >
            <Input v-model="form.areaNumber" clearable style="width:570px"/>
          </FormItem> -->
          <FormItem label="区域经理" prop="areaManger"  >
            <Input v-model="form.areaManger" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="业务员" prop="yeWu"  >
            <Input v-model="form.yeWu" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="客服" prop="keFu"  >
            <Input v-model="form.keFu" clearable style="width:570px"/>
          </FormItem>
          <!-- <FormItem label="省份" prop="sheng"  >
            <Input v-model="form.sheng" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="地级市" prop="shi"  >
            <Input v-model="form.shi" clearable style="width:570px"/>
          </FormItem> -->
          <FormItem label="联系人" prop="callPeople"  >
            <Input v-model="form.callPeople" clearable style="width:570px"/>
          </FormItem>
          <!-- <FormItem label="市县镇区" prop="peopleForm"  >
            <Input v-model="form.peopleForm" clearable style="width:570px"/>
          </FormItem> -->
          <FormItem label="街道" prop="jieDao"  >
            <Input v-model="form.jieDao" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="建材市场" prop="jianCai"  >
            <Input v-model="form.jianCai" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="备注" prop="remark"  >
            <Input v-model="form.remark" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="开户行" prop="kaiHang"  >
            <Input v-model="form.kaiHang" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="银行账户" prop="yinHang"  >
            <Input v-model="form.yinHang" clearable style="width:570px"/>
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
import { editKeHuRoster,getKeShangTypeNotParent,getAllShengShiXian,getXianByName,getShinByName } from "./api.js";
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
      ban: true,
      huType: "",
      shangNumber: "",
      shangName: "",
      huForm: "",
      shangForm: "",
      xinMoney: "",
      huLevel: "",
      huLiang: "",
      fenSi: "",
      bigQu: "",
      cityType: "",
      cityLevel: "",
      xianType: "",
      dianMi: "",
      sellNumber: "",
      areaNumber: "",
      areaManger: "",
      yeWu: "",
      keFu: "",
      sheng: "",
      shi: "",
      callPeople: "",
      peopleForm: "",
      jieDao: "",
      jianCai: "",
      remark: "",
      kaiHang: "",
      yinHang: "",
    },
    // 表单验证规则
    formValidate: {
    },
    cityTemp:'',
    keShangTypeList:[],
      cityData:[],
      cityDataAns:'',
    };
  },
  methods: {
    init() { 
      this.handleReset();
      this.form = this.data;
      this.getKeShangTypeNotParentFx();
      this.getAllShengShiXianFx();
      this.cityTemp = this.form.fenSi + ' / ' +this.form.bigQu + ' / ' +this.form.areaNumber + ' / ' +this.form.sheng + ' / ' +this.form.shi + ' / ' +this.form.peopleForm;
    },
    changeCityData(e1,e2){
      var that = this;
      // console.log(e1); // 简略数组
      // console.log(e2); // 完整数组
      this.form.fenSi = e1[0];
      this.form.bigQu = e1[1];
      this.form.areaNumber = e1[2];
      this.form.sheng = e1[3];
      this.form.shi = e1[4];
      this.form.peopleForm = e1[5];
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
    getAllShengShiXianFx(){
      var that = this;
      getAllShengShiXian().then(res => {
        console.log(res);
        that.cityData = res.result;
      })
    },
    changeShangType(e){
      this.form.shangForm = e.title;
    },
    getKeShangTypeNotParentFx(){
      var that = this;
      getKeShangTypeNotParent().then(res => {
        // console.log(res);
        that.keShangTypeList = res.result;
      })
    },
    changeShengShiQu(e){
      // console.log(e);
      this.form.sheng = e[0].code;
      this.form.shi = e[1].code;
      this.form.peopleForm = e[2].code;
    },
    changeShengShiQu(e){
      // console.log(e);
      this.form.sheng = e[0].code;
      this.form.shi = e[1].code;
      this.form.peopleForm = e[2].code;
    },
    handleReset() {
      this.$refs.form.resetFields();
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          editKeHuRoster(this.form).then(res => {
            this.submitLoading = false;
            if (res.success) {
              this.$Message.success("操作成功");
              this.submited();
            }
          });
        }
      });
    },
    close() {
      this.$emit("close", true);
    },
    submited() {
      this.$emit("submited", true);
    }
  },
  mounted() {
    this.init();
  }
};
</script>
<style lang="less">
// 建议引入通用样式 具体路径自行修改 可删除下面样式代码
// @import "../../../styles/single-common.less";
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