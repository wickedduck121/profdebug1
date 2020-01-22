<template>
  <div id="app">
    <faculty-table/>
  </div>
</template>

<script>
import FacultyTable from "./components/FacultyTable";

export default {
  name: 'app',
  components: {
    FacultyTable
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
