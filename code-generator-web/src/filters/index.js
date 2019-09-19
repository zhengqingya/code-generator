// set function parseTime,formatTime to filter
// 时间格式化所用
import moment from "moment";

export { parseTime, formatTime } from '@/utils'

function pluralize(time, label) {
  if (time === 1) {
    return time + label
  }
  return time + label + 's'
}

export function timeAgo(time) {
  const between = Date.now() / 1000 - Number(time)
  if (between < 3600) {
    return pluralize(~~(between / 60), ' minute')
  } else if (between < 86400) {
    return pluralize(~~(between / 3600), ' hour')
  } else {
    return pluralize(~~(between / 86400), ' day')
  }
}

/* 数字 格式化*/
export function numberFormatter(num, digits) {
  const si = [
    { value: 1E18, symbol: 'E' },
    { value: 1E15, symbol: 'P' },
    { value: 1E12, symbol: 'T' },
    { value: 1E9, symbol: 'G' },
    { value: 1E6, symbol: 'M' },
    { value: 1E3, symbol: 'k' }
  ]
  for (let i = 0; i < si.length; i++) {
    if (num >= si[i].value) {
      return (num / si[i].value + 0.1).toFixed(digits).replace(/\.0+$|(\.[0-9]*[1-9])0+$/, '$1') + si[i].symbol
    }
  }
  return num.toString()
}

export function toThousandFilter(num) {
  return (+num || 0).toString().replace(/^-?\d+/g, m => m.replace(/(?=(?!\b)(\d{3})+$)/g, ','))
}

// 下面郑清个人所用======================================================================================

// 时间格式化
export function dateTimeFilter(date) {
  if (date != null) {
    return moment(date).format("YYYY-MM-DD HH:mm:ss");
  } else {
    return "";
  }
}

// 日期格式化
export function dateFilter(date) {
  if (date != null) {
    return moment(date).format("YYYY-MM-DD");
  } else {
    return "";
  }
}

import {tempalteType, dbType} from "@/data/basic_data";
//格式化模板类型
export function formatTemplateType(type) {
  let selectedType = tempalteType.find(row => {
    return row.value == type;
  });
  if (selectedType) {
    // console.log(selectedType.label);
    return selectedType.label;
  }
}

//格式化数据库类型
export function formatDbType(type) {
  let selectedType = dbType.find(row => {
    return row.value == type;
  });
  if (selectedType) {
    // console.log(selectedType.label);
    return selectedType.label;
  }
}
