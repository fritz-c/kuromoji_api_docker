# kuromoji-api

Simple POST API for [Kuromoji](https://github.com/atilika/kuromoji) Japanese text analysis.

## Usage

```sh
docker run -d -p 3000:3000 --rm chrisus/kuromoji-api:1.0.1

curl -X POST -H "Content-Type: application/json" 'localhost:3000' -d '{"body": "これはテストです。" }'
# Returns:
# {"tokens":[{"surface":"これ","position":0,"features":["名詞","代名詞","一般","*","*","*","これ","コレ","コレ"]},{"surface":"は","position":2,"features":["助詞","係助詞","*","*","*","*","は","ハ","ワ"]},{"surface":"テスト","position":3,"features":["名詞","サ変接続","*","*","*","*","テスト","テスト","テスト"]},{"surface":"です","position":6,"features":["助動詞","*","*","*","特殊・デス","基本形","です","デス","デス"]},{"surface":"。","position":8,"features":["記号","句点","*","*","*","*","。","。","。"]}]}
```

## Build
```sh
mvn clean package
```
