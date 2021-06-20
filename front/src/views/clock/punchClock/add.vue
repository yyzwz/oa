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
      <Form ref="form" :model="form" :label-width="100" :rules="formValidate" label-position="left" >          <FormItem label="工号" prop="userId"  >
            <Input v-model="form.userId" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="用户姓名" prop="userName"  >
            <Input v-model="form.userName" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="年份" prop="year"  >
            <Select v-model="form.year" clearable style="width:570px">
              <Option
                v-for="(item, i) in this.$store.state.dict.year"
                :key="i"
                :value="item.value"
              >{{item.title}}</Option>
            </Select>
          </FormItem>
          <FormItem label="月份" prop="mouth"  >
            <Select v-model="form.mouth" clearable style="width:570px">
              <Option
                v-for="(item, i) in this.$store.state.dict.mouth"
                :key="i"
                :value="item.value"
              >{{item.title}}</Option>
            </Select>
          </FormItem>
          <FormItem label="day1" prop="day1"  >
            <Input v-model="form.day1" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day2" prop="day2"  >
            <Input v-model="form.day2" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day3" prop="day3"  >
            <Input v-model="form.day3" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day4" prop="day4"  >
            <Input v-model="form.day4" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day5" prop="day5"  >
            <Input v-model="form.day5" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day6" prop="day6"  >
            <Input v-model="form.day6" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day7" prop="day7"  >
            <Input v-model="form.day7" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day8" prop="day8"  >
            <Input v-model="form.day8" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day9" prop="day9"  >
            <Input v-model="form.day9" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day10" prop="day10"  >
            <Input v-model="form.day10" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day11" prop="day11"  >
            <Input v-model="form.day11" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day12" prop="day12"  >
            <Input v-model="form.day12" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day13" prop="day13"  >
            <Input v-model="form.day13" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day14" prop="day14"  >
            <Input v-model="form.day14" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day15" prop="day15"  >
            <Input v-model="form.day15" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day16" prop="day16"  >
            <Input v-model="form.day16" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day17" prop="day17"  >
            <Input v-model="form.day17" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day18" prop="day18"  >
            <Input v-model="form.day18" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day19" prop="day19"  >
            <Input v-model="form.day19" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day20" prop="day20"  >
            <Input v-model="form.day20" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day21" prop="day21"  >
            <Input v-model="form.day21" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day22" prop="day22"  >
            <Input v-model="form.day22" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day23" prop="day23"  >
            <Input v-model="form.day23" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day24" prop="day24"  >
            <Input v-model="form.day24" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day25" prop="day25"  >
            <Input v-model="form.day25" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day26" prop="day26"  >
            <Input v-model="form.day26" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day27" prop="day27"  >
            <Input v-model="form.day27" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day28" prop="day28"  >
            <Input v-model="form.day28" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day29" prop="day29"  >
            <Input v-model="form.day29" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day30" prop="day30"  >
            <Input v-model="form.day30" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="day31" prop="day31"  >
            <Input v-model="form.day31" clearable style="width:570px"/>
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
import { addPunchClock } from "./api.js";
export default {
  name: "add",
  components: {
    },
  data() {
    return {
      submitLoading: false, // 表单提交状态
      form: { // 添加或编辑表单对象初始化数据
        userId: "",
        userName: "",
        year: "",
        mouth: "",
        day1: "",
        day2: "",
        day3: "",
        day4: "",
        day5: "",
        day6: "",
        day7: "",
        day8: "",
        day9: "",
        day10: "",
        day11: "",
        day12: "",
        day13: "",
        day14: "",
        day15: "",
        day16: "",
        day17: "",
        day18: "",
        day19: "",
        day20: "",
        day21: "",
        day22: "",
        day23: "",
        day24: "",
        day25: "",
        day26: "",
        day27: "",
        day28: "",
        day29: "",
        day30: "",
        day31: "",
      },
      // 表单验证规则
      formValidate: {
        userId: [{ required: true, message: "不能为空", trigger: "blur" }],
        userName: [{ required: true, message: "不能为空", trigger: "blur" }],
        year: [{ required: true, message: "不能为空", trigger: "blur" }],
        mouth: [{ required: true, message: "不能为空", trigger: "blur" }],
        day1: [{ required: true, message: "不能为空", trigger: "blur" }],
        day2: [{ required: true, message: "不能为空", trigger: "blur" }],
        day3: [{ required: true, message: "不能为空", trigger: "blur" }],
        day4: [{ required: true, message: "不能为空", trigger: "blur" }],
        day5: [{ required: true, message: "不能为空", trigger: "blur" }],
        day6: [{ required: true, message: "不能为空", trigger: "blur" }],
        day7: [{ required: true, message: "不能为空", trigger: "blur" }],
        day8: [{ required: true, message: "不能为空", trigger: "blur" }],
        day9: [{ required: true, message: "不能为空", trigger: "blur" }],
        day10: [{ required: true, message: "不能为空", trigger: "blur" }],
        day11: [{ required: true, message: "不能为空", trigger: "blur" }],
        day12: [{ required: true, message: "不能为空", trigger: "blur" }],
        day13: [{ required: true, message: "不能为空", trigger: "blur" }],
        day14: [{ required: true, message: "不能为空", trigger: "blur" }],
        day15: [{ required: true, message: "不能为空", trigger: "blur" }],
        day16: [{ required: true, message: "不能为空", trigger: "blur" }],
        day17: [{ required: true, message: "不能为空", trigger: "blur" }],
        day18: [{ required: true, message: "不能为空", trigger: "blur" }],
        day19: [{ required: true, message: "不能为空", trigger: "blur" }],
        day20: [{ required: true, message: "不能为空", trigger: "blur" }],
        day21: [{ required: true, message: "不能为空", trigger: "blur" }],
        day22: [{ required: true, message: "不能为空", trigger: "blur" }],
        day23: [{ required: true, message: "不能为空", trigger: "blur" }],
        day24: [{ required: true, message: "不能为空", trigger: "blur" }],
        day25: [{ required: true, message: "不能为空", trigger: "blur" }],
        day26: [{ required: true, message: "不能为空", trigger: "blur" }],
        day27: [{ required: true, message: "不能为空", trigger: "blur" }],
        day28: [{ required: true, message: "不能为空", trigger: "blur" }],
        day29: [{ required: true, message: "不能为空", trigger: "blur" }],
        day30: [{ required: true, message: "不能为空", trigger: "blur" }],
        day31: [{ required: true, message: "不能为空", trigger: "blur" }],
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
          addPunchClock(this.form).then(res => {
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