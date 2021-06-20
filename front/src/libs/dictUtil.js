import axios from 'axios';
import { getDictData } from '@/api/index';

let dictUtil = {

};

// 获取常用的数据字典保存至vuex
dictUtil.initDictData = function (vm) {
    axios.get(getDictData + "sex").then(res => {
        if(res.success){
            vm.$store.commit("setSex", res.result);
        }
    });
    axios.get(getDictData + "message_type").then(res => {
        if(res.success){
            vm.$store.commit("setMessageType", res.result);
        }
    });
    axios.get(getDictData + "priority").then(res => {
        if(res.success){
            vm.$store.commit("setPriority", res.result);
        }
    });
    axios.get(getDictData + "leave_type").then(res => {
        if(res.success){
            vm.$store.commit("setLeaveType", res.result);
        }
    });
    // 民族
    axios.get(getDictData + "nation").then(res => {
        if(res.success){
            vm.$store.commit("setNation", res.result);
        }
    });
    // 婚姻状态
    axios.get(getDictData + "marriage").then(res => {
        if(res.success){
            vm.$store.commit("setMarriage", res.result);
        }
    });
    // 政治面貌
    axios.get(getDictData + "face").then(res => {
        if(res.success){
            vm.$store.commit("setFace", res.result);
        }
    });
    // 保险类型
    axios.get(getDictData + "insuranceType").then(res => {
        if(res.success){
            vm.$store.commit("setInsuranceType", res.result);
        }
    });
    // 年
    axios.get(getDictData + "year").then(res => {
        if(res.success){
            vm.$store.commit("setYear", res.result);
        }
    });
    // 月
    axios.get(getDictData + "mouth").then(res => {
        if(res.success){
            vm.$store.commit("setMouth", res.result);
        }
    });
    // 房型
    axios.get(getDictData + "houseType").then(res => {
        if(res.success){
            vm.$store.commit("setHouseType", res.result);
        }
    });
    // 资产类型
    axios.get(getDictData + "assets").then(res => {
        if(res.success){
            vm.$store.commit("setAssets", res.result);
        }
    });
    // 罚款原因
    axios.get(getDictData + "fineReason").then(res => {
        if(res.success){
            vm.$store.commit("setFineReason", res.result);
        }
    });
    // 房屋配置
    axios.get(getDictData + "houseSetting").then(res => {
        if(res.success){
            vm.$store.commit("setHouseSetting", res.result);
        }
    });
};

export default dictUtil;
