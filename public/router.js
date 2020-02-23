import Memberpage from "./pages/Member.page.js";
import Memberspage from "./pages/Members.page.js";
import Projectpage from "./pages/Project.page.js";
import Projectspage from "./pages/Projects.page.js";
import HomePage from "./pages/Home.page.js";

const router = new VueRouter({
  routes: [
    {
      path: "/",
      component: HomePage
    },
    {
      path: "/members",
      component: Memberspage
    },
    {
      path: "/member/:id(.*)",
      name: "member",
      component: Memberpage,
      props: true
    },
    {
      path: "/projects",
      component: Projectspage
    },
    {
      path: "/project/:id(.*)",
      name: "project",
      component: Projectpage,
      props: true
    }
  ]
});

export default router;
