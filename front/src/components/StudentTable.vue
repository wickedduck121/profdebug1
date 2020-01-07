<template>
    <div>
    <el-table
            :data="tableData"
            style="width: 100%"
            max-height="250">
        <el-table-column
                fixed
                label="Чл. Проф."
                width="100">
            <template slot-scope="scope">
                <el-switch v-model="scope.row.prof"> </el-switch>
            </template>

        </el-table-column>
        <el-table-column
                prop="pib"
                label="ФИО"
                width="250">
        </el-table-column>
        <el-table-column
                prop="group"
                label="Группа"
                width="120">
        </el-table-column>
        <el-table-column
                label="Пол"
                width="120">
            <template slot-scope="scope">
            <el-switch
                    style="display: block"
                    v-model="scope.row.gender"
                    active-color="#F000D7"
                    inactive-color="#00A9F9"
                    active-text="Ж"
                    inactive-text="М">
            </el-switch>
            </template>
        </el-table-column>
        <el-table-column
                prop="code"
                label="Идент. код"
                width="120">
        </el-table-column>
        <el-table-column
                prop="date"
                label="Дата рождения"
                width="150">
        </el-table-column>
        <el-table-column
                prop="address"
                label="Адресс"
                width="300">
        </el-table-column>
        <el-table-column
                fixed="right"
                label="Operations"
                width="120">
            <template slot-scope="scope">
                <el-button
                        @click.native.prevent="deleteRow(scope.$index, tableData, scope.row.idStud)"
                        type="text"
                        size="small">
                    Remove
                </el-button>
            </template>
        </el-table-column>
    </el-table>
        <el-button type="primary" @click="add()">Добавить</el-button>

        <el-collapse-transition>
        <div id="adding" v-show="show">
        <el-form ref="form" :model="form" label-width="120px">
            <el-form-item label="ФИО">
                <el-input v-model="form.pib"> </el-input>
            </el-form-item>
            <el-form-item label="Группа">
                    <el-input v-model="form.group"> </el-input>
            </el-form-item>
            <el-form-item label="Пол">
                <el-switch
                        style="display: block"
                        v-model="form.gender"
                        active-color="#F000D7"
                        inactive-color="#00A9F9"
                        active-text="Женский"
                        inactive-text="Мужской">
                </el-switch>
            </el-form-item>
            <el-form-item label="Идентификационный Код" label-width="200px">
                <el-input v-model="form.code"> </el-input>
            </el-form-item>
            <el-form-item label="Дата рождения">
                <el-col :span="11">
                    <el-date-picker type="date" placeholder="Pick a date" v-model="form.date" style="width: 100%;"> </el-date-picker>
                </el-col>
            </el-form-item>
            <el-form-item label="Адрес">
                <el-input v-model="form.address"> </el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit()">Create</el-button>
                <el-button @click="hide()">Cancel</el-button>
            </el-form-item>
        </el-form>
    </div>
        </el-collapse-transition>
    </div>

</template>

<script>
    import { allStudents,  deleteStudent, addStudent } from '../api.js';
    export default {
        name: "StudentTable",
        mounted() {
            allStudents().then(res => res.data.forEach(el=>{
                var dat = el.date.split("T");
                const val = {
                    idStud: el.idStud,
                    prof: el.prof,
                    pib:el.pib,
                    group:el.group,
                    gender:el.gender,
                    code:el.code,
                    date: dat[0],
                    address:el.address};

                this.tableData.push(val)}));
        },
        methods: {
            updateTab(){
                this.tableData = [];
                allStudents().then(res => res.data.forEach(el=>{
                    var dat = el.date.split("T");
                    const val = {
                        idStud: el.idStud,
                        prof: el.prof,
                        pib:el.pib,
                        group:el.group,
                        gender:el.gender,
                        code:el.code,
                        date: dat[0],
                        address:el.address};

                    this.tableData.push(val)}));
            },
            deleteRow(index, rows, idLoc) {
                deleteStudent(idLoc).then(res=>{
                    alert(res.data);
                    if (res.data!=null) {rows.splice(index, 1);}
                });

            },
            add(){
                this.show=true;
            },
            hide(){
                    this.form.pib= '';
                this.form.group= '';
                this.form.gender=false;
                this.form.code='';
                this.form.date= '';
                this.form.address='';
                this.show=false;
            },
            onSubmit(){
                addStudent(
                    this.form.pib,
                    this.form.group,
                    this.form.gender,
                    this.form.code,
                    this.form.date,
                    this.form.address).then(res=>{
                        if (res.data!=null){
                            this.form.pib= '';
                            this.form.group= '';
                            this.form.gender=false;
                            this.form.code='';
                            this.form.date= '';
                            this.form.address='';
                            this.show=false;
                            this.updateTab();
                        }
                        else{
                            alert("error!");
                        }
                });

            }

        },
        data() {
            return {
                form: {
                    pib: '',
                    group: '',
                    gender:false,
                    code:'',
                    date: '',
                   address:''
                },
                tableData: [],
                show:false
            }
        }
    }

</script>

<style scoped>
#adding{
    position: absolute;
    width: 50vw;
    top: 40px;
    left: 25vw;
    background-color: lightblue;
    border: 1px solid blue;
    border-radius: 8px;
    /*background-color: black;*/
}
</style>