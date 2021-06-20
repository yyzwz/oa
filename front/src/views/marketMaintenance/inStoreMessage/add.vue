<template>
  <div>
    <Card>
      <div slot="title">
        <div class="edit-head">
          <a @click="close" class="back-title">
            <Icon type="ios-arrow-back" />返回
          </a>
          <div class="head-name">添加</div>
          <span></span>
          <a @click="close" class="window-close">
            <Icon type="ios-close" size="31" class="ivu-icon-ios-close" />
          </a>
        </div>
      </div>
      <Form ref="form" :model="form" :label-width="100" :rules="formValidate" label-position="left" >          <FormItem label="负责人" prop="fuZeRen"  >
            <Input v-model="form.fuZeRen" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="销售负责人" prop="xiaoShouRen"  >
            <Input v-model="form.xiaoShouRen" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="到店日期" prop="date"  >
            <Input v-model="form.date" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="店名" prop="dianName"  >
            <Input v-model="form.dianName" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="关系" prop="guanXi"  >
            <Input v-model="form.guanXi" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="面积" prop="areaSize"  >
            <Input v-model="form.areaSize" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="位置" prop="weiZhi"  >
            <Input v-model="form.weiZhi" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="定位" prop="dingWei"  >
            <Input v-model="form.dingWei" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="纬度" prop="latitude"  >
            <Input v-model="form.latitude" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="经度" prop="longitude"  >
            <Input v-model="form.longitude" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="版本数量" prop="banBenShuLiang"  >
            <Input v-model="form.banBenShuLiang" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="纯净度" prop="cunJingDu"  >
            <Input v-model="form.cunJingDu" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="艾是占比" prop="aiShiZhanBi"  >
            <Input v-model="form.aiShiZhanBi" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="物流信息" prop="wuLiuXinXi"  >
            <Input v-model="form.wuLiuXinXi" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="人员规模" prop="renYuanGuiMo"  >
            <Input v-model="form.renYuanGuiMo" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="经营评估" prop="jinYingPinGu"  >
            <Input v-model="form.jinYingPinGu" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="评分1" prop="fen1"  >
            <Input v-model="form.fen1" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="评分2" prop="fen2"  >
            <Input v-model="form.fen2" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="评分3" prop="fen3"  >
            <Input v-model="form.fen3" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="点评" prop="about"  >
            <Input v-model="form.about" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="照片" prop="imageList"  >
            <Input v-model="form.imageList" clearable style="width:570px"/>
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
import { addDaoDianMessage } from "./api.js";
export default {
  name: "add",
  components: {
    },
  data() {
    return {
      submitLoading: false, // 表单提交状态
      form: { // 添加或编辑表单对象初始化数据
        fuZeRen: "",
        xiaoShouRen: "",
        date: "",
        dianName: "",
        guanXi: "",
        areaSize: "",
        weiZhi: "",
        dingWei: "",
        latitude: "",
        longitude: "",
        banBenShuLiang: "",
        cunJingDu: "",
        aiShiZhanBi: "",
        wuLiuXinXi: "",
        renYuanGuiMo: "",
        jinYingPinGu: "",
        fen1: "",
        fen2: "",
        fen3: "",
        about: "",
        imageList: "",
      },
      // 表单验证规则
      formValidate: {
      }
    };
  },
  methods: {
    init() {},
    handleReset() {
      this.$refs.form.resetFields();
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          addDaoDianMessage(this.form).then(res => {
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