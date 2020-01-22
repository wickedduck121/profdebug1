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
                <el-button type="primary" @click="add()">Добавить</el-button>
                <el-button type="primary" @click="hideFaculty()">Вернуться</el-button>
            </div>
        </el-collapse-transition>

        <el-collapse-transition>
            <div v-show="showFacultyAdd" class="forms">
                <el-form ref="form" :model="formCreate" label-width="120px">
                    <el-form-item label="Название">
                        <el-input v-model="formCreate.facName"> </el-input>
                    </el-form-item>
                    <el-form-item label="Аббревиатура">
                        <el-input v-model="formCreate.abbreviation"> </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="onSubmitAdd()">Create</el-button>
                        <el-button @click="hideCreate()">Cancel</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-collapse-transition>

        <el-collapse-transition>
            <div v-show="showFacultyUpdate" class="forms">
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
                        <el-button @click="hideUpdate()">Отмена</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-collapse-transition>

        <el-collapse-transition>
            <div v-show="showStudent">
                <student-table :facultyData="facultyData" :show-func="showFacTable"/>
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
            }
        },
        mounted() {
            getAllFaculty().then(res=>{
                if (res.status === 200){

                    res.data.forEach(el=>{
                        this.facultyData.push(el);
                    });
                    console.log(this.facultyData);
                }
                else{
                    alert("Ошибка при получении данных!")
                }
            })
        },
        methods:{
            handleEdit(ind, row){
                this.formUpdate.facId = row.facId;
                this.formUpdate.facName = row.facName;
                this.formUpdate.abbreviation = row.abbreviation;
                this.showFacultyUpdate = true;
                this.showFaculty = false;
            },
            add(){
                this.showFaculty=false;
                this.showFacultyAdd=true;
            },
            hideCreate(){
              this.formCreate.facName = '';
              this.formCreate.abbreviation = '';
              this.showFacultyAdd = false;
              this.showFaculty = true;
            },
            hideUpdate(){
                this.formUpdate.facId = '';
                this.formUpdate.facName = '';
                this.formUpdate.abbreviation = '';
                this.showFacultyUpdate = false;
                this.showFaculty = true;
            },
            onSubmitAdd(){
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
                            message: 'Добавлено успешно'
                        });
                        this.hideCreate();
                    }
                    else{
                        alert("Ошибка при добавлении!");
                    }
                });
            },
            onSubmitUpdate(){
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
                            message: 'Обновлено успешно'
                        });
                        this.hideUpdate();
                    }
                    else{
                        alert("Ошибка при обновлении!");
                    }
                });
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
                                this.$message({
                                    type: 'success',
                                    message: 'Удалено'
                                });
                            }
                            else{
                                alert("Ошибка удаления!");
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