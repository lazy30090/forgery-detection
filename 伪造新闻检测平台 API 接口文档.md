# 伪造新闻检测平台 API 接口文档


**HOST**:http://localhost:8080


**联系人**:lazy

**Version**:1.0


**接口路径**:/v3/api-docs/全部接口


[TOC]






# 1. 新闻管理模块


## 新增-上传新闻


**接口地址**:`/api/news/add`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>用于用户手动上传待检测的新闻。系统会自动将标签设为&#39;待检测&#39;，来源设为&#39;用户上传&#39;。</p>



**请求示例**:


```javascript
{
  "id": 0,
  "newsId": "",
  "title": "",
  "content": "",
  "publishTime": "",
  "platform": "",
  "author": "",
  "url": "",
  "picUrl": "",
  "checkTime": "",
  "label": "",
  "hashtag": "",
  "dataSource": 0,
  "createTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|news|News|body|true|News|News|
|&emsp;&emsp;id|||false|integer(int64)||
|&emsp;&emsp;newsId|||false|string||
|&emsp;&emsp;title|||true|string||
|&emsp;&emsp;content|||true|string||
|&emsp;&emsp;publishTime|||false|string(date-time)||
|&emsp;&emsp;platform|||false|string||
|&emsp;&emsp;author|||false|string||
|&emsp;&emsp;url|||false|string||
|&emsp;&emsp;picUrl|||false|string||
|&emsp;&emsp;checkTime|||false|string(date-time)||
|&emsp;&emsp;label|||false|string||
|&emsp;&emsp;hashtag|||false|string||
|&emsp;&emsp;dataSource|||false|integer(int32)||
|&emsp;&emsp;createTime|||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ApiResponseLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int64)|integer(int64)|
|message||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": 0
}
```


## 获取新闻详情


**接口地址**:`/api/news/{id}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>根据主键 ID 查询单条新闻详情</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|新闻主键ID|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ApiResponseNews|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int64)|integer(int64)|
|message||string||
|data||News|News|
|&emsp;&emsp;id||integer(int64)||
|&emsp;&emsp;newsId||string||
|&emsp;&emsp;title||string||
|&emsp;&emsp;content||string||
|&emsp;&emsp;publishTime||string(date-time)||
|&emsp;&emsp;platform||string||
|&emsp;&emsp;author||string||
|&emsp;&emsp;url||string||
|&emsp;&emsp;picUrl||string||
|&emsp;&emsp;checkTime||string(date-time)||
|&emsp;&emsp;label||string||
|&emsp;&emsp;hashtag||string||
|&emsp;&emsp;dataSource||integer(int32)||
|&emsp;&emsp;createTime||string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {
		"id": 0,
		"newsId": "",
		"title": "",
		"content": "",
		"publishTime": "",
		"platform": "",
		"author": "",
		"url": "",
		"picUrl": "",
		"checkTime": "",
		"label": "",
		"hashtag": "",
		"dataSource": 0,
		"createTime": ""
	}
}
```


## 分页查询新闻列表


**接口地址**:`/api/news/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>支持按标题模糊搜索，按标签筛选，结果按创建时间倒序排列</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|page|页码 (默认1)|query|false|integer(int32)||
|size|每页条数 (默认10)|query|false|integer(int32)||
|title|搜索关键词 (匹配标题)|query|false|string||
|label|筛选标签 (如: 谣言/事实)|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ApiResponseIPageNews|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int64)|integer(int64)|
|message||string||
|data||IPageNews|IPageNews|
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;records||array|News|
|&emsp;&emsp;&emsp;&emsp;id||integer(int64)||
|&emsp;&emsp;&emsp;&emsp;newsId||string||
|&emsp;&emsp;&emsp;&emsp;title||string||
|&emsp;&emsp;&emsp;&emsp;content||string||
|&emsp;&emsp;&emsp;&emsp;publishTime||string(date-time)||
|&emsp;&emsp;&emsp;&emsp;platform||string||
|&emsp;&emsp;&emsp;&emsp;author||string||
|&emsp;&emsp;&emsp;&emsp;url||string||
|&emsp;&emsp;&emsp;&emsp;picUrl||string||
|&emsp;&emsp;&emsp;&emsp;checkTime||string(date-time)||
|&emsp;&emsp;&emsp;&emsp;label||string||
|&emsp;&emsp;&emsp;&emsp;hashtag||string||
|&emsp;&emsp;&emsp;&emsp;dataSource||integer(int32)||
|&emsp;&emsp;&emsp;&emsp;createTime||string(date-time)||
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;pages||integer(int64)||
|&emsp;&emsp;total||integer(int64)||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {
		"size": 0,
		"records": [
			{
				"id": 0,
				"newsId": "",
				"title": "",
				"content": "",
				"publishTime": "",
				"platform": "",
				"author": "",
				"url": "",
				"picUrl": "",
				"checkTime": "",
				"label": "",
				"hashtag": "",
				"dataSource": 0,
				"createTime": ""
			}
		],
		"current": 0,
		"pages": 0,
		"total": 0
	}
}
```


# 3. 通用工具模块


## 图片上传接口


**接口地址**:`/api/common/upload`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>上传图片文件，返回可访问的 URL 地址</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|file||query|true|file||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ApiResponseString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int64)|integer(int64)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": ""
}
```


# 2. 伪造检测模块


## 提交检测任务


**接口地址**:`/api/detect/submit`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>传入新闻ID，后台创建一个异步检测任务，并返回任务ID</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|newsId|新闻ID|query|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ApiResponseLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int64)|integer(int64)|
|message||string||
|data||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": 0
}
```


## 查询检测进度-结果


**接口地址**:`/api/detect/result/{taskId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>前端需要轮询此接口。status: 0-待检测, 1-检测中, 2-完成。当 status=2 时，字段 confidence 和 explanation 会有值。</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|taskId|任务ID (submit接口返回的)|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ApiResponseDetectionTask|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int64)|integer(int64)|
|message||string||
|data||DetectionTask|DetectionTask|
|&emsp;&emsp;id||integer(int64)||
|&emsp;&emsp;newsId||integer(int64)||
|&emsp;&emsp;status||integer(int32)||
|&emsp;&emsp;resultLabel||string||
|&emsp;&emsp;confidence||number||
|&emsp;&emsp;explanation||string||
|&emsp;&emsp;modelVersion||string||
|&emsp;&emsp;costTime||integer(int64)||
|&emsp;&emsp;createTime||string(date-time)||
|&emsp;&emsp;updateTime||string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {
		"id": 0,
		"newsId": 0,
		"status": 0,
		"resultLabel": "",
		"confidence": 0,
		"explanation": "",
		"modelVersion": "",
		"costTime": 0,
		"createTime": "",
		"updateTime": ""
	}
}
```


# 4. 数据统计模块


## 获取首页仪表盘数据


**接口地址**:`/api/stats/dashboard`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>返回新闻总数、谣言数、事实数等统计指标</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ApiResponseMapStringObject|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int64)|integer(int64)|
|message||string||
|data||object||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {}
}
```