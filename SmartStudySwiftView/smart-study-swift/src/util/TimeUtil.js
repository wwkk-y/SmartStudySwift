// 时间处理工具类

/**
 * 如果是今天，就返回字符串今天的时分，如果是昨天就返回字符串’昨天‘，如果是今年就返回月日，否则返回年月日
 * @param {String|Date} dateString 
 * @returns 
 */
export function formatDateString(date) {
    if(!date){
        return '';
    }

    let givenDate = date;
    if(!(date instanceof Date)){
         // 解析输入的日期字符串
        givenDate = new Date(date);
    }
    const now = new Date();

    // 获取今天的开始时间和昨天的开始时间
    const todayStart = new Date();
    todayStart.setHours(0, 0, 0, 0);  // 设置到今天开始
    const yesterdayStart = new Date(todayStart.getTime() - 24 * 60 * 60 * 1000);  // 减去一天

    // 检查是否是今天
    if (givenDate >= todayStart && givenDate <= now) {
        return `${givenDate.getHours().toString().padStart(2, '0')}:${givenDate.getMinutes().toString().padStart(2, '0')}`;
    }
    // 检查是否是昨天
    else if (givenDate >= yesterdayStart && givenDate < todayStart) {
        return '昨天';
    }
    // 检查是否是今年
    else if (givenDate.getFullYear() === now.getFullYear()) {
        return `${givenDate.getMonth() + 1}-${givenDate.getDate()}`;  // 注意月份从0开始计数
    }
    // 其他情况
    else {
        return `${givenDate.getFullYear()}-${(givenDate.getMonth() + 1).toString().padStart(2, '0')}-${givenDate.getDate().toString().padStart(2, '0')}`;
    }
}

export default {
    formatDateString
}