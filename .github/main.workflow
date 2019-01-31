workflow "Workflow - Push" {
  resolves = ["package"]
  on = "push"
}

action "package" {
  uses = "LucaFeger/action-maven-cli@master"
  args = "clean install"
}
