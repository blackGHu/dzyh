(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-34e5ba8c"],{"010f":function(e,r,s){"use strict";var o=s("23cd"),n=s.n(o);n.a},"23cd":function(e,r,s){},a819:function(e,r,s){},b953:function(e,r,s){"use strict";s.r(r);var o=function(){var e=this,r=e.$createElement,s=e._self._c||r;return s("div",{staticClass:"login-container"},[s("el-form",{ref:"loginForm",staticClass:"login-form",attrs:{model:e.loginForm,rules:e.loginRules,"auto-complete":"on","label-position":"left"}},[s("div",{staticClass:"title-container"},[s("h3",{staticClass:"title"},[e._v("注册账户")])]),s("el-form-item",{attrs:{prop:"userId"}},[s("span",{staticClass:"svg-container"},[s("svg-icon",{attrs:{"icon-class":"user"}})],1),s("el-input",{ref:"userId",attrs:{placeholder:"userId",name:"userId",type:"text",tabindex:"1","auto-complete":"on"},model:{value:e.loginForm.userId,callback:function(r){e.$set(e.loginForm,"userId",r)},expression:"loginForm.userId"}})],1),s("el-form-item",{attrs:{prop:"username"}},[s("span",{staticClass:"svg-container"},[s("svg-icon",{attrs:{"icon-class":"user"}})],1),s("el-input",{ref:"username",attrs:{placeholder:"Username",name:"username",type:"text",tabindex:"1","auto-complete":"on"},model:{value:e.loginForm.userName,callback:function(r){e.$set(e.loginForm,"userName",r)},expression:"loginForm.userName"}})],1),s("el-form-item",{attrs:{prop:"password"}},[s("span",{staticClass:"svg-container"},[s("svg-icon",{attrs:{"icon-class":"password"}})],1),s("el-input",{key:e.passwordType,ref:"password",attrs:{type:e.passwordType,placeholder:"Password",name:"password",tabindex:"2","auto-complete":"on"},nativeOn:{keyup:function(r){return!r.type.indexOf("key")&&e._k(r.keyCode,"enter",13,r.key,"Enter")?null:e.handleRegister(r)}},model:{value:e.loginForm.userPassword,callback:function(r){e.$set(e.loginForm,"userPassword",r)},expression:"loginForm.userPassword"}}),s("span",{staticClass:"show-pwd",on:{click:e.showPwd}},[s("svg-icon",{attrs:{"icon-class":"password"===e.passwordType?"eye":"eye-open"}})],1)],1),s("el-form-item",{attrs:{prop:"password"}},[s("span",{staticClass:"svg-container"},[s("svg-icon",{attrs:{"icon-class":"password"}})],1),s("el-input",{key:e.passwordType,ref:"Ensure_Password",attrs:{type:e.passwordType,placeholder:"Ensure_Password",name:"Ensure_Password",tabindex:"2","auto-complete":"on"},nativeOn:{keyup:function(r){return!r.type.indexOf("key")&&e._k(r.keyCode,"enter",13,r.key,"Enter")?null:e.handleRegister(r)}},model:{value:e.ensurePassword,callback:function(r){e.ensurePassword=r},expression:"ensurePassword"}}),s("span",{staticClass:"show-pwd",on:{click:e.showPwd}},[s("svg-icon",{attrs:{"icon-class":"password"===e.passwordType?"eye":"eye-open"}})],1)],1),s("el-form-item",{attrs:{prop:"Phone"}},[s("span",{staticClass:"svg-container"},[s("svg-icon",{attrs:{"icon-class":"user"}})],1),s("el-input",{ref:"Phone",attrs:{placeholder:"Phone Number",name:"Phone",type:"text",tabindex:"1","auto-complete":"on"},nativeOn:{keyup:function(r){return!r.type.indexOf("key")&&e._k(r.keyCode,"enter",13,r.key,"Enter")?null:e.handleRegister(r)}},model:{value:e.loginForm.userPhone,callback:function(r){e.$set(e.loginForm,"userPhone",r)},expression:"loginForm.userPhone"}})],1),s("el-radio-group",{model:{value:e.loginForm.roleId,callback:function(r){e.$set(e.loginForm,"roleId",r)},expression:"loginForm.roleId"}},[s("el-radio",{attrs:{label:1}},[e._v("游客")]),s("el-radio",{attrs:{label:2}},[e._v("教师")]),s("el-radio",{attrs:{label:3}},[e._v("学生")])],1),s("el-button",{staticStyle:{width:"100%","margin-top":"20px","margin-bottom":"30px"},attrs:{loading:e.loading,type:"primary"},nativeOn:{click:function(r){return r.preventDefault(),e.handleRegister(r)}}},[e._v("注册")])],1)],1)},n=[],t=s("c24f"),a={name:"Login",data:function(){var e=function(e,r,s){null==r?s(new Error("Please enter the correct user ID")):s()},r=function(e,r,s){r.length<6?s(new Error("The password can not be less than 6 digits")):s()},s=function(e,r,s){null==r?s(new Error("Please enter the correct user name")):s()},o=function(e,r,s){11!==r.length?s(new Error("Please enter the correct phone number")):s()},n=function(e,r,s){null==r?s(new Error("Please choose role")):s()};return{loginForm:{userId:null,userPassword:null,userName:null,userPhone:null,roleId:null},loginRules:{userId:[{required:!0,trigger:"blur",validator:e}],userPassword:[{required:!0,trigger:"blur",validator:r}],userName:[{required:!0,trigger:"blur",validator:s}],userPhone:[{required:!0,trigger:"blur",validator:o}],roleId:[{required:!0,trigger:"blur",validator:n}]},ensurePassword:null,loading:!1,passwordType:"password",redirect:void 0,user_role:null}},watch:{$route:{handler:function(e){this.redirect=e.query&&e.query.redirect},immediate:!0}},methods:{showPwd:function(){var e=this;"password"===this.passwordType?this.passwordType="":this.passwordType="password",this.$nextTick((function(){e.$refs.password.focus()}))},handleRegister:function(){var e=this;null!=this.loginForm.userName&&null!=this.loginForm.userPassword||this.$message({type:"error",message:"用户名或密码错误"}),this.loginForm.userPassword!==this.ensurePassword?this.$message({type:"error",message:"两次密码不一致"}):this.$refs.loginForm.validate((function(r){if(null==e.loginForm.roleId)e.$message({type:"error",message:"请选择一个角色！"});else{if(!r)return e.$message({type:"error",message:"用户名或密码错误"}),!1;e.loading=!0,console.log({userId:e.loginForm.userId,userPassword:e.loginForm.userPassword,userName:e.loginForm.userName,userPhone:e.loginForm.userPhone,roleId:e.loginForm.roleId}),Object(t["d"])({userId:e.loginForm.userId,userPassword:e.loginForm.userPassword,userName:e.loginForm.userName,userPhone:e.loginForm.userPhone,roleId:e.loginForm.roleId}).then((function(r){e.$message({message:"已发送注册请求，除游客外请等待管理员审批",type:"success"}),e.loading=!1})).catch((function(r){console.log("注册出错"+r),e.loading=!1}))}}))}}},l=a,i=(s("f28e"),s("010f"),s("2877")),u=Object(i["a"])(l,o,n,!1,null,"f849db4a",null);r["default"]=u.exports},f28e:function(e,r,s){"use strict";var o=s("a819"),n=s.n(o);n.a}}]);