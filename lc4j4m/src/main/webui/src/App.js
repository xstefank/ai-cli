import axios from 'axios';
import {useState} from "react";
import TextareaAutosize from 'react-textarea-autosize';
import {Commet} from "react-loading-indicators";


function App() {

  const [prompt, setPrompt] = useState('');
  const [model, setModel] = useState('gemini-1.5-flash');
  const [temp, setTemp] = useState('1.0');
  const [topk, setTopK] = useState('');
  const [topp, setTopP] = useState('');

  const loading = document.getElementById("loading-box");
  const llmOutput = document.getElementById("llm-output-wrapper");


  let submit = async (e) => {
    if (e != undefined) e.preventDefault();

    loading.style.display = "flex";
    llmOutput.style.display = "none";
    try {
      await axios.post("http://localhost:8123/chat", {
        "prompt": prompt,
        "model": model,
        "temperature": temp,
        "topK": topk,
        "topP": topp
      }).then(value => {
        loading.style.display = "none";
        llmOutput.style.display = "block";
        document.getElementById("llm-output").value = value.data;
      });
    } catch (e) {
      console.log(e);
    }
  }

  let onEnterPress = (e) => {
    if (e.keyCode === 13 && e.ctrlKey === true) {
      submit();
    }
  }

  return (
    <div className="main-container">
      <h2 className="llm-title">lc4j4m</h2>

      <form action="#" className="llm-form" onSubmit={submit} id="llmForm">
        <div className="input-wrapper">

          <label>Model: </label>
          <select onChange={(e) => {
            setModel(e.target.value)
          }} className="hyperparam">
            <option value="gemini-1.5-flash">gemini-1.5-flash</option>
            <option value="gemini-1.5-pro">gemini-1.5-pro</option>
            <option value="gemini-2.0-flash">geemini-2.0-flash</option>
            <option value="gpt-4o-mini">gpt-4o-mini</option>
            <option value="gpt-4.1">gpt-4.1</option>
          </select>
          <label>Temperature: </label><input type="text" className="hyperparam" onChange={(e) => {
          setTemp(e.target.value)
        }}/>
          <label>Top-K: </label><input type="text" className="hyperparam" onChange={(e) => {
          setTopK(e.target.value)
        }}/>
          <label>Top-P:</label><input type="text" className="hyperparam" onChange={(e) => {
          setTopP(e.target.value)
        }}/>
          <TextareaAutosize onChange={(e) => {
            setPrompt(e.target.value)
          }} className="llm-input" cols="30" rows="10" placeholder="Enter your prompt here..." onKeyDown={onEnterPress}/>
        </div>

        <input type="submit" className="llm-button" value="Send"/>
      </form>

      <div className="loading-box" id="loading-box">
        <Commet color={["#32cd32", "#327fcd", "#cd32cd", "#cd8032"]}/>
      </div>

      <div className="output-wrapper" id="llm-output-wrapper">
        <textarea id="llm-output" name="output" cols="30" rows="10" className="llm-output" placeholder="Output will be shown here..." required/>
      </div>

    </div>
  )
}

export default App