<template>
  <div id="app">
    <student-table> </student-table>
  </div>
</template>

<script>
import StudentTable from "./components/StudentTable";
import UploadFile from "./components/UploadFile";

export default {
  name: 'app',
  components: {
    StudentTable
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
