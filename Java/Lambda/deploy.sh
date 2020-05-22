echo 'Choose the application to deploy'
echo '1 -> ApiGateway Application'
read -p 'Please enter you choice (numbers): ' choice

if [ $choice == "1" ]; then
  echo 'Deploying ApiGateway Application'
  templateFile='template_apigateway.yaml'
else
  echo 'Invalid application chosen to deploy. Exiting..'
  exit
fi


echo 'Building the module'
sam build --template-file $templateFile

if [ $? -eq 0 ]; then
    echo 'Successfully built the module..'
else
    echo 'Error creating building the module'
fi

echo 'Deploying the package with sam deploy'
sam deploy --guided --capabilities CAPABILITY_IAM

if [ $? -eq 0 ]; then
    echo 'Deploy of the package completed'
else
    echo 'Failed to deploy the lambda to aws. Please check the output for the errors'
fi