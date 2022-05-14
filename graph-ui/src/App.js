import './App.css';
import Main from "./components/Main";
import {createTheme, CssBaseline, ThemeProvider} from "@mui/material";

function App() {
    return (
        <div className="App">
            <ThemeProvider theme={createTheme({palette: {mode: 'dark'}})}>
                <CssBaseline/>
                <Main/>
            </ThemeProvider>
        </div>
    );
}

export default App;
