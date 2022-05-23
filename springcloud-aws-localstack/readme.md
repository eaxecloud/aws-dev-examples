# 这个项目是做什么的？

这个项目是用来演示如何如何使用Localstack开发AWS的程序。



# LocalStack是什么？

LocalStack为开发云应用程序提供了一个易于使用的测试/模拟框架。 它在您的本地机器上构建了一个测试环境，该环境提供了与真正的AWS云环境相同的功能和api。  

> Localstack官网：https://localstack.cloud/



# 运行工程前的准备

## 1、准备Localstack环境。

安装并启动Localstack。请参考https://docs.localstack.cloud/get-started/

启动后，可以运行以下命令验证，有类似以下的输出说明启动成功了

```shell
#在运行localstack的服务器上运行，或SSH远程上去运行。
$ localstack status services
┏━━━━━━━━━━━━━━━━━━━━━━━━━━┳━━━━━━━━━━━━━┓
┃ Service                  ┃ Status      ┃
┡━━━━━━━━━━━━━━━━━━━━━━━━━━╇━━━━━━━━━━━━━┩
│ acm                      │ ✔ available │
│ apigateway               │ ✔ available │
│ cloudformation           │ ✔ available │
│ cloudwatch               │ ✔ available │
│ config                   │ ✔ available │
│ dynamodb                 │ ✔ running   │
│ dynamodbstreams          │ ✔ available │
│ ec2                      │ ✔ available │
│ es                       │ ✔ available │
│ events                   │ ✔ available │
│ firehose                 │ ✔ available │
│ iam                      │ ✔ available │
│ kinesis                  │ ✔ available │
│ kms                      │ ✔ available │
│ lambda                   │ ✔ available │
│ logs                     │ ✔ available │
│ opensearch               │ ✔ available │
│ redshift                 │ ✔ available │
│ resource-groups          │ ✔ available │
│ resourcegroupstaggingapi │ ✔ available │
│ route53                  │ ✔ available │
│ route53resolver          │ ✔ available │
│ s3                       │ ✔ available │
│ s3control                │ ✔ available │
│ secretsmanager           │ ✔ available │
│ ses                      │ ✔ available │
│ sns                      │ ✔ running   │
│ sqs                      │ ✔ running   │
│ ssm                      │ ✔ available │
│ stepfunctions            │ ✔ available │
│ sts                      │ ✔ available │
│ support                  │ ✔ available │
│ swf                      │ ✔ available │
└──────────────────────────┴─────────────┘

```

或在浏览器上访问：http://192.168.146.179:4566/health （可以远程访问，不限在localstack本机上运行）

## 2、初始化一些数据。

```shell
#endpoint-url的实际URL，需要根据实际运行localstack的服务器地址而定。如果localstack与application运行在同一服务器上，IP地址可以使用localhost
aws --endpoint-url=http://192.168.146.179:4566 sqs create-queue  --queue-name  order-queue
aws --endpoint-url=http://192.168.146.179:4566 sns create-topic  --name  order-created-topic
aws --endpoint-url=http://192.168.146.179:4566 sqs create-queue  --queue-name  order-queue-2
aws --endpoint-url=http://192.168.146.179:4566 sns subscribe \
--topic-arn arn:aws:sns:us-east-1:000000000000:order-created-topic \
--protocol sqs \
--notification-endpoint http://192.168.146.179:4566/000000000000/order-queue \

aws --endpoint-url=http://192.168.146.179:4566 sns subscribe \
--topic-arn arn:aws:sns:us-east-1:000000000000:order-created-topic \
--protocol sqs \
--notification-endpoint http://192.168.146.179:4566/000000000000/order-queue-2 \


aws --endpoint-url=http://192.168.146.179:4566 dynamodb create-table \
--table-name User \
--attribute-definitions \
	AttributeName=id,AttributeType=S \
	AttributeName=firstName,AttributeType=S \
--key-schema \
	AttributeName=id,KeyType=HASH \
	AttributeName=firstName,KeyType=RANGE \
--provisioned-throughput \
	ReadCapacityUnits=5,WriteCapacityUnits=5 \
--table-class STANDARD \


```



验证创建的资源

```shell
aws --endpoint-url=http://192.168.146.179:4566 sqs list-queues
aws --endpoint-url=http://192.168.146.179:4566 sns list-subscriptions
aws --endpoint-url=http://192.168.146.179:4566 dynamodb list-tables
```



> 需要注意的是，如果运行Localstack的服务器与运行此工程不在同一服务器上，在启动localstack时需要设置环境变量HOSTNAME_EXTERNAL，值是运行Localstack的服务器地址。
>
> 比如，以docker方式启动时，命令如下:
>
> ```shell
> docker run -ti --rm --name my-localstack -d -p 4566:4566 -p 4510-4559:4510-4559 -e DEBUG=1 -e "LOCALSTACK_SERVICES=s3,sqs,sns,dynamodb" -e HOSTNAME_EXTERNAL=192.168.146.179 localstack/localstack
> ```



# 接口测试

User接口的测试

``` shell
curl -H "Content-Type:application/json" -X POST --data '{"firstName":"Elon","lastName":"Musk"}' "http://localhost:8080/user"

curl -X GET "http://localhost:8080/user"
curl -X GET "http://localhost:8080/user?firstName=Elon&lastName=Musk"
curl -X GET "http://localhost:8080/user?firstName=Elon"
curl -X GET "http://localhost:8080/user?lastName=Musk"
curl -X GET "http://localhost:8080/user?lastName=Tom"
```

> 在Window上运行curl时，json数据格式如下，否则会报错。
>
> ```shell
> curl -H "Content-Type:application/json" -X POST --data "{"""firstName""":"""Elon""","""lastName""":"""Musk"""}" "http://localhost:8080/user"
> ```



其他接口请参考工程的controller中的类。
