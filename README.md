# kuromoji-api

Simple POST API for [Kuromoji](https://github.com/atilika/kuromoji) Japanese text analysis.

## Usage

```sh
docker run -d -p 9696:9696 --rm chrisus/kuromoji-api

curl -X POST -H "Content-Type: application/json" 'localhost:9696' -d '{ "mode":"normal", "body": "これはテストです。" }'
# Returns:
# {"tokens":[
#   {"surface":"これ","position":0,"features":["名詞","代名詞","一般","*","*","*","これ","コレ","コレ"]},
#   {"surface":"は","position":2,"features":["助詞","係助詞","*","*","*","*","は","ハ","ワ"]},
#   {"surface":"テスト","position":3,"features":["名詞","サ変接続","*","*","*","*","テスト","テスト","テスト"]},
#   {"surface":"です","position":6,"features":["助動詞","*","*","*","特殊・デス","基本形","です","デス","デス"]},
#   {"surface":"。","position":8,"features":["記号","句点","*","*","*","*","。","。","。"]}
# ]}
```

Sample POST payload:

```json
{
  "body": "<Japanese text goes here>",
  "mode": "<Optional; default `normal`; one of `normal`, `search`, or `extended`>"
}
```

## Development

```sh
# Build jar and prepare dependencies
mvn package

# Execute jar file
bin/run-dev.sh
```

## Releasing

```sh
# Update version in pom.xml
vim pom.xml

git add pom.xml
git commit -m "chore(release): update to v1.x.x"

# Update version in pom.xml
git tag v1.x.x

# Generate changelog
npx conventional-changelog -p angular -i CHANGELOG.md -s -r 0
git add CHANGELOG.md
git commit -m "chore(changelog): update changelog"

git push
git push --tags
```
