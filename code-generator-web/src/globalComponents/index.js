import Vue from 'vue'

function registerAllComponents(requireContext) {
  return requireContext.keys().forEach(comp => {
    const vueComp = requireContext(comp)
    const compName = vueComp.default.name ? vueComp.default.name : /\/([\w-]+)\.vue$/.exec(comp)[1]
    Vue.component(`cus${getKebabCase(compName)}`, vueComp.default)
  })
}
function getKebabCase(str) {
  return str.replace(/[A-Z]/g, function(i) {
    return '-' + i.toLowerCase()
  })
}
registerAllComponents(require.context('./', true, /\.vue$/))
