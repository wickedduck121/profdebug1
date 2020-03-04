<template>
    <div>
    <el-menu
             id="menu"
             class="el-menu-vertical"
             @open="handleOpen"
             @close="handleClose"
             @select="handleSelect"
             :collapse="isCollapse">
        <el-button :icon="icon" @click="colChange" circle> </el-button>
        <el-menu-item index="1">
            <i class="el-icon-user-solid"></i>
            <span slot="title">Войти</span>
        </el-menu-item>
    </el-menu>
        <el-dialog title="Введите данные входа" :visible.sync="loginShow" @close="handleCloseLogin">
            <el-form
            ref="form" :model="loginFormData"
            label-width="80px"
            size="mini"
            >
            <el-form-item label="Логин">
                <el-input v-model="loginFormData.usName"> </el-input>
            </el-form-item>
            <el-form-item label="Пароль">
                <el-input v-model="loginFormData.pass" type="password"> </el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmitLogin()">Войти</el-button>
                <el-button type="primary" @click="regMenuShow()">Регистрация</el-button>
            </el-form-item>
            </el-form>
        </el-dialog>
        <el-dialog title="Введите данные регистрации" :visible.sync="regShow" @close="handleCloseReg">
            <el-form
                    ref="form" :model="regFormData"
                    label-width="80px"
                    size="mini"
            >
                <el-form-item label="Логин">
                    <el-input v-model="regFormData.usName"> </el-input>
                </el-form-item>
                <el-form-item label="Пароль">
                    <el-input v-model="regFormData.pass" type="password"> </el-input>
                </el-form-item>
                <el-form-item label="E-mail">
                    <el-input v-model="regFormData.email"> </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmitReg()">Зарегистрироваться</el-button>
                    <el-button type="primary" @click="onRegCancel()">Вернуться</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <faculty-table :is-collapse="isCollapseText"/>


    </div>
</template>

<script>
    import {login, register} from "../api";
    import FacultyTable from "./FacultyTable";
    export default {
        name: "Navigation",
        components:{
          FacultyTable
        },
            data() {
                return {
                    isCollapse: true,
                    isCollapseText: 'floatRightCollapsed',
                    icon: 'el-icon-caret-right',
                    loginShow:false,
                    regShow: false,
                    loginFormData:{
                        usName:'',
                        pass:''
                    },
                    regFormData:{
                        usName:'',
                        pass:'',
                        email:''
                    }
                };
            },
            methods: {
            onRegCancel(){
                this.regFormData.usName = '';
                this.regFormData.pass = '';
                this.regFormData.email = '';
                this.regShow = false;

            },
            regMenuShow(){
              this.regShow = true;
            },
            enterMenuShow(){
                 this.loginShow = true;
                let routeData = this.$router.resolve({name: 'routeName', query: {data: "someData"}});
                window.open(routeData.href, '_blank');
            },
            onSubmitLogin(){
                login(this.loginFormData.usName, this.loginFormData.pass);
                this.handleCloseLogin();
            },
            onSubmitReg(){
                register(this.regFormData.usName, this.regFormData.pass, this.regFormData.email);
                this.handleCloseReg();
            },
            handleCloseReg(){
                this.regFormData.usName = '';
                this.regFormData.pass = '';
                this.regFormData.email = '';
               // this.regShow = false;
                //this.handleCloseLogin();
                //this.loginShow = true;
            },
            handleCloseLogin(){
               this.loginFormData.usName = '';
               this.loginFormData.pass = '';
               this.loginShow = false;
               //не работает
               var myElement = document.getElementById("menu");
               myElement.setAttribute('default-active','')
                //конец участка
            },
            handleOpen(key, keyPath) {
                    console.log(key, keyPath);
            },
            handleClose(key, keyPath) {
                    console.log(key, keyPath);
            },
            handleSelect(key, keyPath) {
                if(key==='1'){
                    this.enterMenuShow();
                }
            },
            colChange(){
                   // alert(this.isCollapse);
                    this.isCollapse = !this.isCollapse;
                  if(this.isCollapse===true){
                        this.isCollapseText = 'floatRightCollapsed';
                        this.icon = 'el-icon-caret-right';
                  }
                  else{
                        this.isCollapseText = 'floatRightNotCollapsed';
                        this.icon = 'el-icon-caret-left';
                  }
                }
            }

    }
</script>

<style scoped>
    .el-menu-vertical{
        position: fixed;
        height: 100vh;
    }
    .el-menu-vertical:not(.el-menu--collapse) {
        width: 200px;
        min-height: 400px;
    }

    .tab{
        width: 85%;
        float: right;
    }
</style>

