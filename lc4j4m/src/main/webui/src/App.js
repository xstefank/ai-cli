import axios from 'axios';
import {useState} from "react";
import TextareaAutosize from 'react-textarea-autosize';


function App() {

  const [prompt, setPrompt] = useState('');
  const [model, setModel] = useState('gemini-1.5-flash');
  const [temp, setTemp] = useState('1.0');
  const [topk, setTopK] = useState('');
  const [topp, setTopP] = useState('');

  let submit = async (e) => {
    try {
      await axios.post("http://localhost:8123/chat", {
        "prompt": prompt,
        "model": model,
        "temperature": temp,
        "topK": topk,
        "topP": topp
      }).then(value => {
        document.getElementById("llm-output").value = value.data;
      });
    } catch (e) {
      console.log(e);
    }
  }

  let onEnterPress = (e) => {
    if(e.keyCode === 13 && e.ctrlKey === true) {
      submit();
    }
  }

  return (
    <div className="main-container">
      <h2 className="llm-title">lc4j4m</h2>

      <form action="#" className="llm-form" onSubmit={submit} id="llmForm">
        <div className="input-wrapper">

          <label>Model: </label><select name="model" value={model} onChange={(e) => {
            setModel(e.target.value)
        }} className="hyperparam" list="model-list">
          <option value="gemini-2.0-flash">gemini-2.0-flash</option>
          <option value="gemini-1.5-flash">gemini-1.5-flash</option>
          <option value="gemini-1.5-pro">gemini-1.5-pro</option>
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
          }} className="llm-input" cols="30" rows="10" placeholder="Enter your prompt here..." onKeyDown={onEnterPress} />
        </div>

        <input type="submit" className="llm-button" value="Send"/>
      </form>

      <div className="output-wrapper">
        <textarea id="llm-output" name="output" cols="30" rows="10" className="llm-output" placeholder="Output will be shown here..." required/>
      </div>

    </div>
  )
}

export default App