curl -v http://192.168.43.1:6624/osc/commands/execute -d '{"name":"camera.takePicture"}}'; echo
curl -v -H 'Content-Type: application/json;charset=utf-8' http://192.168.43.1:6624/osc/commands/execute -d '{"name":"camera.takePicture"}'; echo
curl -v -H 'Content-Type: application/json;charset=utf-8' http://192.168.43.1:6624/osc/commands/status -d '{"id":"CCSd93c7"}' | python -m json.tool
curl -v -H 'Content-Type: application/json;charset=utf-8' http://192.168.43.1:6624/osc/commands/execute -d '{"name":"camera.listFiles"}' | python -m json.tool > list.files.error.json
curl -v -H 'Content-Type: application/json;charset=utf-8' http://192.168.43.1:6624/osc/commands/execute -d '{"name":"camera.listFiles", "parameters":{"fileType":"image","entryCount":"3"}}' | python -m json.tool | tee list.3.files.json
curl -v -H 'Content-Type: application/json;charset=utf-8' http://192.168.43.1:6624/osc/commands/execute -d '{"name":"camera.getOptions", "parameters":{"optionNames":["wifiPassword"]}}' | python -m json.tool | tee options.wifipassword.json




