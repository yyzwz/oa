const dict = {
    state: {
        // 经常需要读取的数据字典
        sex: [],
        messageType: [],
        priority: [],
        leaveType: [],
        nation:[], // 民族
        marriage:[], // 婚姻状态
        face:[], // 政治面貌
        insuranceType:[], // 保险类型
        year:[], // 年
        mouth:[], // 月
        houseType:[], // 房型
        assets:[], //资产类型
        fineReason:[], //罚款原因
        houseSetting:[], // 房屋配置
    },
    mutations: {
        // 设置值的改变方法
        setSex(state, list) {
            state.sex = list;
        },
        setMessageType(state, list) {
            state.messageType = list;
        },
        setPriority(state, list) {
            state.priority = list;
        },
        setLeaveType(state, list) {
            state.leaveType = list;
        },
        // 民族
        setNation(state, list) {
            state.nation = list;
        },
        // 婚姻状态
        setMarriage(state, list) {
            state.marriage = list;
        },
        // 政治面貌
        setFace(state, list) {
            state.face = list;
        },
        // 保险类型
        setInsuranceType(state, list) {
            state.insuranceType = list;
        }, 
        // 年
        setYear(state, list) {
            state.year = list;
        }, 
        // 月
        setMouth(state, list) {
            state.mouth = list;
        }, 
        // 房型
        setHouseType(state, list) {
            state.houseType = list;
        }, 
        // 资产类型
        setAssets(state, list) {
            state.assets = list;
        }, 
        // 罚款原因
        setFineReason(state, list) {
            state.fineReason = list;
        },
        // 房屋配置
        setHouseSetting(state, list) {
            state.houseSetting = list;
        },
    }
};

export default dict;
