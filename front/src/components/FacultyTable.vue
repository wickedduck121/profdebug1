<template>
    <div>


        <el-collapse-transition>
            <div v-show="showFaculty">
                <el-table
                        ref="tab"
                        style="width: 100%"
                        :data="facultyData"
                        :default-sort="{prop: 'facName', order: 'descending'}"
                        lazy
                        max-height="500px">

                    <el-table-column
                            prop="facName"
                            label="Название"
                            sortable
                            width="500">
                    </el-table-column>

                    <el-table-column
                            prop="abbreviation"
                            label="Сокр. назв."
                            sortable
                            width="120">
                    </el-table-column>

                    <el-table-column
                            align="right">
                        <template slot-scope="scope">
                            <el-button
                                    size="mini"
                                    @click="handleEdit(scope.$index, scope.row)">Редактировать</el-button>
                            <el-button
                                    size="mini"
                                    type="danger"
                                    @click.native.prevent="deleteRow(scope.row)">
                                Удалить
                            </el-button>
                        </template>
                    </el-table-column>

                </el-table>
                <el-button type="primary" @click="dialogAdd = true">Добавить</el-button>
                <el-button type="primary" @click="hideFaculty()">Вернуться</el-button>
            </div>
        </el-collapse-transition>

        <el-drawer
                title="Введите необходимые данные"
                :before-close="handleCloseAdd"
                :visible.sync="dialogAdd"
                direction="ltr"
                custom-class="drawer"
                ref="drawerAdd"
                size="50%"
        >
            <div style="padding: 20px">
                <el-form ref="form" :model="formCreate" >
                    <el-form-item label="Название">
                        <el-input v-model="formCreate.facName"> </el-input>
                    </el-form-item>
                    <el-form-item label="Аббревиатура">
                        <el-input v-model="formCreate.abbreviation"> </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="onSubmitAdd()">Create</el-button>
                        <el-button @click="cancelAdd()">Cancel</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-drawer>

        <el-drawer
                title="Введите необходимые данные"
                :before-close="handleCloseUpdate"
                :visible.sync="dialogUpdate"
                direction="ltr"
                custom-class="drawer"
                ref="drawerAdd"
                size="50%"
        >
            <div>
                <el-form
                        ref="form" :model="formUpdate"
                        label-width="120px"
                        size="mini"
                >
                    <el-form-item label="Название">
                        <el-input v-model="formUpdate.facName"> </el-input>
                    </el-form-item>
                    <el-form-item label="Аббревиатура">
                        <el-input v-model="formUpdate.abbreviation"> </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="onSubmitUpdate()">Сохранить</el-button>
                        <el-button @click="cancelUpdate()">Отмена</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-drawer>

        <el-collapse-transition>
            <div v-show="showStudent">
                <student-table
                        :facultyData="facultyData"
                        :show-func="showFacTable"
                        :fac-filter="propFilter"
                />
            </div>
        </el-collapse-transition>
    </div>
</template>

<script>
    import StudentTable from "./StudentTable";
    import {getAllFaculty, addFaculty, updateFaculty, delFaculty} from "../api";

    export default {
        name: "FacultyTable",
        components:{
            StudentTable
        },
        data(){
            return{
                facultyData: [],
                propFilter:[],
                showStudent: true,
                showFaculty: false,
                showFacultyAdd: false,
                showFacultyUpdate: false,
                formCreate: {
                    facName: '',
                    abbreviation: ''
                },
                formUpdate: {
                    facId: '',
                    facName: '',
                    abbreviation: ''
                },
                dialogAdd: false,
                dialogUpdate: false
            }
        },
        mounted() {
            getAllFaculty().then(res=>{
                if (res.status === 200){

                    res.data.forEach(el=>{
                        this.facultyData.push(el);
                    });
                    this.propFilterChange();
                    //console.log(this.facultyData);
                }
                else{
                    this.$message.error('Ошибка при получении данных');
                }
            })
        },
        methods:{
            addCheck(){
                for(var i1=0;i1<this.formCreate.facName.length;i1++){
                    if (this.formCreate.facName[i1].toLowerCase() < 'а' ||
                        this.formCreate.facName[i1].toLowerCase() > 'я' &&
                        this.formCreate.facName[i1] !== '’' &&
                        this.formCreate.facName[i1] !== "'") {;
                        return 'Название факультета должно состоять из букв';
                    }
                }
                for(var i2=0;i2<this.formCreate.abbreviation.length;i2++) {
                    if (this.formCreate.abbreviation[i2].toLowerCase() < 'а' ||
                        this.formCreate.abbreviation[i2].toLowerCase() > 'я' &&
                        this.formCreate.abbreviation[i2] !== '’' &&
                        this.formCreate.abbreviation[i2] !== "'") {
                        return 'ФИО должно состоять только из букв';
                    }
                }
                if(this.formCreate.facName.length===0){return 'Заполните поле названия факультета'}
                if(this.formCreate.abbreviation.length===0){return 'Заполните поле аббревиатуры'}
                return '';
            },
            updateCheck(){
                for(var i1=0;i1<this.formUpdate.facName.length;i1++){
                    if (this.formUpdate.facName[i1].toLowerCase() < 'а' ||
                        this.formUpdate.facName[i1].toLowerCase() > 'я' &&
                        this.formUpdate.facName[i1] !== '’' &&
                        this.formUpdate.facName[i1] !== "'") {;
                        return 'Название факультета должно состоять из букв';
                    }
                }
                for(var i2=0;i2<this.formUpdate.abbreviation.length;i2++) {
                    if (this.formUpdate.abbreviation[i2].toLowerCase() < 'а' ||
                        this.formUpdate.abbreviation[i2].toLowerCase() > 'я' &&
                        this.formUpdate.abbreviation[i2] !== '’' &&
                        this.formUpdate.abbreviation[i2] !== "'") {
                        return 'ФИО должно состоять только из букв';
                    }
                }
                if(this.formUpdate.facName.length===0){return 'Заполните поле названия факультета'}
                if(this.formUpdate.abbreviation.length===0){return 'Заполните поле аббревиатуры'}
                return '';
            },
            propFilterChange(){
                this.propFilter = [];
                for(var i=0;i<this.facultyData.length;i++){
                    const el = {
                        text: this.facultyData[i].abbreviation,
                        value: this.facultyData[i].facId
                    };
                    this.propFilter.push(el);
                }
            },
            handleCloseAdd(){
                    this.hideCreate();
                this.$message({
                    message: 'Добавление отменено',
                    center: true
                });
            },
            handleCloseUpdate(){
                this.hideUpdate();
                this.$message({
                    message: 'Редактирование отменено',
                    center: true
                });
            },
            cancelAdd(){
                this.$message({
                    message: 'Добавление отменено',
                    center: true
                });
                this.hideCreate();
            },
            cancelUpdate(){
                this.$message({
                    message: 'Редактирование отменено',
                    center: true
                });
                this.hideUpdate();
            },
            handleEdit(ind, row){
                this.formUpdate.facId = row.facId;
                this.formUpdate.facName = row.facName;
                this.formUpdate.abbreviation = row.abbreviation;
                this.dialogUpdate = true;
            },
            add(){
                this.showFaculty=false;
                this.showFacultyAdd=true;
            },
            hideCreate(){
              this.formCreate.facName = '';
              this.formCreate.abbreviation = '';
              this.dialogAdd = false;
              //this.showFacultyAdd = false;
              //this.showFaculty = true;
            },
            hideUpdate(){
                this.formUpdate.facId = '';
                this.formUpdate.facName = '';
                this.formUpdate.abbreviation = '';
                this.dialogUpdate = false;
            },
            onSubmitAdd(){
                const resCheck = this.addCheck();
                if(resCheck===''){
                addFaculty(this.formCreate.facName, this.formCreate.abbreviation).then(res=>{
                    if(res.status===200){
                        const el = {
                            facId: res.data.facId,
                            facName: res.data.facName,
                            abbreviation: res.data.abbreviation
                        };
                        this.facultyData.push(el);
                        this.$message({
                            type: 'success',
                            message: 'Добавлено успешно',
                            center: true
                        });
                        this.propFilterChange();
                        this.hideCreate();
                    }
                    else{
                        this.$message.error('Ошибка при добавлении');
                    }
                });
                }
                else{
                    this.$message.error(resCheck);
                }
            },
            onSubmitUpdate(){
                const resCheck = this.updateCheck();
                if(resCheck===''){
                updateFaculty(
                    this.formUpdate.facId,
                    this.formUpdate.facName,
                    this.formUpdate.abbreviation
                ).then(res=>{
                    if(res.data!==null){
                        const el = {
                            facId: res.data.facId,
                            facName: res.data.facName,
                            abbreviation: res.data.abbreviation
                        };
                        for(var i=0;i<this.facultyData.length;i++){
                            if(this.facultyData[i].facId === el.facId){
                                this.facultyData[i].facName = el.facName;
                                this.facultyData[i].abbreviation = el.abbreviation;
                                break;
                            }
                        }
                        this.$message({
                            type: 'success',
                            message: 'Обновлено успешно',
                            center: true
                        });
                        this.propFilterChange();
                        this.hideUpdate();
                    }
                    else{
                        this.$message.error('Ошибка при обновлении');
                    }
                });}
                else{
                    this.$message.error(resCheck);
                }
            },
            showFacTable(){
                this.showStudent = false;
                this.showFaculty = true;
            },
            hideFaculty(){
              this.showFaculty = false;
              this.showStudent = true;
            },
            deleteRow(row){

                this.$confirm('Это действие очистит поле факультет у студентов с данным факультетом', 'Внимание', {
                    confirmButtonText: 'OK',
                    cancelButtonText: 'Отмена',
                    type: 'warning'
                }).then(() => {
                    delFaculty(row.facId).then(res=>{
                            console.log(res.status);
                            if(res.data!==null){
                                for(var i=0;i<this.facultyData.length;i++){
                                    if(this.facultyData[i].facId===res.data.facId){
                                        this.facultyData.splice(i,1);
                                        break;
                                    }
                                }
                                this.propFilterChange();
                                this.$message({
                                    type: 'success',
                                    message: 'Удалено'
                                });
                            }
                            else{
                                this.$message.error('Ошибка удаления');
                            }
                        }
                    );

                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: 'Удаление отменено'
                    });
                });

            }
        }
    }

</script>

<style scoped>
    .forms{
        border: 1px solid;
        border-radius: 4px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
        position: fixed;
        padding: 20px;
        margin: 0 30vw;
        height: auto;
    }
</style>