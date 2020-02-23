Vue.component("new-project-form", {
  data() {
    return {
      project: {},
      newProjectForm: {
        name: "",
        description: "",
        status: ""
      }
    };
  },
  template: `
 <form @submit="addProject"> 
  <h4>New Project</h4>
        <div class="row">
          <div class="col-25">
            <label for="name">Name</label>
          </div>
          <div class="col-75">
            <input type="text" id="name" name="name" placeholder="project name" v-model="newProjectForm.name" required>
          </div>
        </div> 
          <div class="row">
          <div class="col-25">
            <label for="status">Status</label>
          </div>
          <div class="col-75">
            <input type="text" id="status" name="status" placeholder="project status" v-model="newProjectForm.status" required>
          </div>
        </div>
        <div class="row">
          <div class="col-25">
            <label for="description">Description</label>
          </div>
          <div class="col-75"> 
          <textarea name="description" id="description" cols="30" rows="10" v-model="newProjectForm.description"></textarea> <br> <br>
          </div>
        </div> 
        <div class="row">
          <input type="submit" value="Add project">
        </div>
      </form>
  `,
  methods: {
    addProject(e) { 
      e.preventDefault(); 
      let body = `name=${this.newProjectForm.name}&description=${this.newProjectForm.description}&status=${this.newProjectForm.status}`;
      body = encodeURI(body);
      axios({
        method: "put",
        url: "/api/projects",
        data: body
      })
        .then(function(response) {
          currentObj.output = response.data;
        })
        .catch(function(error) {
          currentObj.output = error;
        });
      this.newProjectForm.name = "";
      this.newProjectForm.status = "";
      this.newProjectForm.description = ""; 
      this.$emit('updated', 'updated')
    }
  }
});

const ProjectsPage = {
  data() {
    return {
      projects: ""
    };
  },
  template: ` 
  <div class="two-split">
<table> 
      <h3>Projects:</h3>
      <tr> 
        <th>Id</th>
        <th>Name</th>
        <th>Description</th> 
        <th>Status</th>
      </tr>
      <tr v-for="project in projects" @click="openProject(project.id)" style="cursor:pointer">
        <td>{{project.id}}</td>
        <td >{{project.name}}</td>
        <td>{{project.description}}</td> 
        <td>{{project.status}}</td>
      </tr>
    </table> 

    <new-project-form @updated="getData()"></new-project-form>
    </div>
  `,
  methods: {
    getData: function() {
      axios
        .get("/api/projects")
        .then(response => (this.projects = response.data));
    },
    openProject(id) {
      this.$router.push("/project/" + id);
    }
  },
  mounted() {
    this.getData();
  }
};

export default ProjectsPage;
