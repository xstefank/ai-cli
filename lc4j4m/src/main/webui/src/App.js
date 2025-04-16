import axios from 'axios';
import {useState} from "react";
import TextareaAutosize from 'react-textarea-autosize';


function App() {

  const [prompt, setPrompt] = useState('');
  const [temp, setTemp] = useState('1.0');
  const [topk, setTopK] = useState('');
  const [topp, setTopP] = useState('');

  let submit = async (e) => {
    e.preventDefault();

    console.log("SAdf");

    try {
      await axios.post("http://localhost:8123/chat", {
        "prompt": prompt,
        "temperature": temp,
        "top_k": topk,
        "top_p": topp
      }).then(value => {
        document.getElementById("llm-output").value = value.data;
      });
    } catch (e) {
      console.log(e);
    }
  }

  return (
    <div className="main-container">
      <h2 className="llm-title">lc4j4m</h2>

      <form action="#" className="llm-form">
        <div className="input-wrapper">

            <label>Temperature: </label><input type="text" className="hyperparam" onChange={(e) => {setTemp(e.target.value)}}/>

          <label>Top-K: </label><input type="text" className="hyperparam" onChange={(e) => {setTopK(e.target.value)}}/>
          <label>Top-P:</label><input type="text" className="hyperparam" onChange={(e) => {setTopP(e.target.value)}}/>
          {/*<textarea name="text" onChange={(e) => {*/}
          {/*  setPrompt(e.target.value)*/}
          {/*}} cols="30" rows="10" className="llm-input" placeholder="Enter your prompt here..." required/>*/}
          <TextareaAutosize onChange={(e) => {setPrompt(e.target.value)}}
                            className="llm-input" cols="30" rows="10" placeholder="Enter your prompt here..." />
        </div>

        <input type="submit" onClick={submit} className="llm-button" value="Send"/>
      </form>

      <div className="output-wrapper">
        <textarea id="llm-output" name="output" cols="30" rows="10" className="llm-output" placeholder="Output will be shown here..." required/>
      </div>

    </div>
  )
}

export default App