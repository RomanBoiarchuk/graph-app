# graph-app deployment (Kubernetes)

- helm repo add bitnami https://charts.bitnami.com/bitnami
- kubectl create namespace graph-app
- helm install kafka bitnami/kafka --namespace graph-app
- helm install postgres bitnami/postgresql --namespace graph-app
- export POSTGRES_PASSWORD=$(kubectl get secret --namespace graph-app postgres-postgresql -o jsonpath="{.data.postgres-password}" | base64 --decode)
- kubectl run postgres-postgresql-client --rm --tty -i --restart='Never' --namespace graph-app  
  --image docker.io/bitnami/postgresql:14.1.0-debian-10-r80 --env="PGPASSWORD=$POSTGRES_PASSWORD"  
  --command -- psql -h postgres-postgresql -U postgres -c "CREATE DATABASE \"vertex_registry\";"
- 
