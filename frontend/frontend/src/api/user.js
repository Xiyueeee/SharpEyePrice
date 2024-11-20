import request from "../utils/request";

/**
 * 获取验证码
 * @param checkKey
 */
export function getCodeInfo(checkKey) {
  return request({
    method: "get",
    url: `/xxx/xxx/${checkKey}`,
  });
}
