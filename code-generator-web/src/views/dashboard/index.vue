<template>
  <div class="dashboard-container">
    <component :is="currentRole" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import myDashboard from './myDashboard'

export default {
  name: 'Dashboard',
  components: { myDashboard },
  data() {
    return {
      currentRole: 'myDashboard'
    }
  },
  computed: {
    ...mapGetters([
      'roles'
    ])
  },
  created() {
    this.currentRole = 'myDashboard'
  }
}
</script>
