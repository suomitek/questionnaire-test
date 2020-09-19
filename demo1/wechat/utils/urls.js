// var host = "http://192.168.43.217";
// var host = "http://192.168.1.103";
// var host = "http://192.168.1.102";
var host = "http://172.20.10.8";
// var host = "http://192.168.1.101";
// var host = "http://127.0.0.1";
// var host = "http://127.0.0.1";
var urlobj = {
  wxLogin: host + "/wx/front/logindo",
  register: host + "/wx/front/register",
  getOpenId: host + "/wx/front/getOpenId",
  editInfo: host + "/wx/front/editInfo",

  banners: host + "/wenjuan/front/banner/listJson",
  wenjuanList: host + "/wenjuan/front/question/listJson",
  votes: host + "/wenjuan/front/vote/listJson",

  joinvotes: host + "/wenjuan/front/vote/join",
  joinwenjuans: host + "/wenjuan/front/question/join",
  footprints: host + "/wenjuan/front/footprint/list",
  

  piliang: host + "/wenjuan/front/question/piliang",
  questionAddSave: host + "/wenjuan/front/question/addSave",
  questionDetailed: host + "/wenjuan/front/question/loadDetailed",
  reply: host + "/wenjuan/front/question/reply",
  questionList: host + "/wenjuan/front/question/list",
  changeSuspend: host + "/wenjuan/front/question/changeSuspend",
  editSave: host + "/wenjuan/front/question/editSave",
  deleteQuestion: host + "/wenjuan/front/question/delete",
  analysisQuestion: host + "/wenjuan/front/question/analysis",
  listAnswerVo: host + "/wenjuan/front/question/listAnswerVo",
  dafen: host + "/wenjuan/front/question/dafen",
  
  voteList: host + "/wenjuan/front/vote/list",
  voteAddSave: host + "/wenjuan/front/vote/addSave",
  voteDetailed: host + "/wenjuan/front/vote/loadDetailed",
  addCandidate: host + "/wenjuan/front/vote/addCandidate",
  listCandidate: host + "/wenjuan/front/vote/listCandidate",
  editCandidate: host + "/wenjuan/front/vote/editCandidate",
  deleteVote: host + "/wenjuan/front/vote/delete",
  
  voterecordAddSave: host + "/wenjuan/front/voterecord/addSave",
  commentAddSave: host + "/wenjuan/front/comment/addSave",
  commentListJson: host + "/wenjuan/front/comment/listJson",
  thumbsSave: host + "/wenjuan/front/commentrecord/addSave",

  listCheck: host + "/wenjuan/front/check/listAll",

  introduceVideoPage: host +"/manage/front/introduceVideo",


  fileImgChangeSave:host+"/basics/file/changeSave",
  fileImgDelete: host + "/basics/file/delete",
  
  singleImgUpload: host + "/basics/admin/uploadify/image/originalSingle",

};
module.exports.urlobj = urlobj;
module.exports.host = host;