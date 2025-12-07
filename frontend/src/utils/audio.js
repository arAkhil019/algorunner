// Simple retro sound synthesizer using Web Audio API

let audioContext = null;

const getAudioContext = () => {
  if (!audioContext) {
    audioContext = new (window.AudioContext || window.webkitAudioContext)();
  }
  return audioContext;
};

export const playMoveSound = () => {
  const ctx = getAudioContext();
  const osc = ctx.createOscillator();
  const gain = ctx.createGain();

  osc.type = 'square';
  osc.frequency.setValueAtTime(150, ctx.currentTime);
  osc.frequency.exponentialRampToValueAtTime(100, ctx.currentTime + 0.1);

  gain.gain.setValueAtTime(0.1, ctx.currentTime);
  gain.gain.exponentialRampToValueAtTime(0.01, ctx.currentTime + 0.1);

  osc.connect(gain);
  gain.connect(ctx.destination);

  osc.start();
  osc.stop(ctx.currentTime + 0.1);
};

export const playCoinSound = () => {
  const ctx = getAudioContext();
  
  // First note
  const osc1 = ctx.createOscillator();
  const gain1 = ctx.createGain();
  osc1.type = 'square';
  osc1.frequency.setValueAtTime(1000, ctx.currentTime);
  gain1.gain.setValueAtTime(0.1, ctx.currentTime);
  gain1.gain.exponentialRampToValueAtTime(0.01, ctx.currentTime + 0.1);
  osc1.connect(gain1);
  gain1.connect(ctx.destination);
  osc1.start();
  osc1.stop(ctx.currentTime + 0.1);

  // Second note (higher)
  const osc2 = ctx.createOscillator();
  const gain2 = ctx.createGain();
  osc2.type = 'square';
  osc2.frequency.setValueAtTime(1500, ctx.currentTime + 0.1);
  gain2.gain.setValueAtTime(0.1, ctx.currentTime + 0.1);
  gain2.gain.exponentialRampToValueAtTime(0.01, ctx.currentTime + 0.3);
  osc2.connect(gain2);
  gain2.connect(ctx.destination);
  osc2.start(ctx.currentTime + 0.1);
  osc2.stop(ctx.currentTime + 0.3);
};

export const playTimerWarningSound = () => {
  const ctx = getAudioContext();
  const osc = ctx.createOscillator();
  const gain = ctx.createGain();

  osc.type = 'sawtooth';
  osc.frequency.setValueAtTime(400, ctx.currentTime);
  osc.frequency.exponentialRampToValueAtTime(200, ctx.currentTime + 0.1);

  gain.gain.setValueAtTime(0.1, ctx.currentTime);
  gain.gain.exponentialRampToValueAtTime(0.01, ctx.currentTime + 0.1);

  osc.connect(gain);
  gain.connect(ctx.destination);

  osc.start();
  osc.stop(ctx.currentTime + 0.1);
};

export const playGameStartSound = () => {
    const ctx = getAudioContext();
    const osc = ctx.createOscillator();
    const gain = ctx.createGain();
  
    osc.type = 'triangle';
    osc.frequency.setValueAtTime(220, ctx.currentTime);
    osc.frequency.linearRampToValueAtTime(440, ctx.currentTime + 0.2);
    osc.frequency.linearRampToValueAtTime(880, ctx.currentTime + 0.4);
  
    gain.gain.setValueAtTime(0.1, ctx.currentTime);
    gain.gain.linearRampToValueAtTime(0.1, ctx.currentTime + 0.4);
    gain.gain.exponentialRampToValueAtTime(0.01, ctx.currentTime + 0.8);
  
    osc.connect(gain);
    gain.connect(ctx.destination);
  
    osc.start();
    osc.stop(ctx.currentTime + 0.8);
};

export const playGameOverSound = (won) => {
    const ctx = getAudioContext();
    const osc = ctx.createOscillator();
    const gain = ctx.createGain();
    
    osc.type = won ? 'square' : 'sawtooth';
    gain.gain.setValueAtTime(0.1, ctx.currentTime);

    osc.connect(gain);
    gain.connect(ctx.destination);
    
    osc.start();

    if (won) {
        // Victory arpeggio
        osc.frequency.setValueAtTime(523.25, ctx.currentTime); // C5
        osc.frequency.setValueAtTime(659.25, ctx.currentTime + 0.2); // E5
        osc.frequency.setValueAtTime(783.99, ctx.currentTime + 0.4); // G5
        osc.frequency.setValueAtTime(1046.50, ctx.currentTime + 0.6); // C6
        gain.gain.exponentialRampToValueAtTime(0.01, ctx.currentTime + 1.5);
        osc.stop(ctx.currentTime + 1.5);
    } else {
        // Defeat slide
        osc.frequency.setValueAtTime(200, ctx.currentTime);
        osc.frequency.linearRampToValueAtTime(50, ctx.currentTime + 1);
        gain.gain.linearRampToValueAtTime(0.01, ctx.currentTime + 1);
        osc.stop(ctx.currentTime + 1);
    }
};

let ambientOscillators = [];
let ambientGain = null;

export const startAmbientMusic = () => {
  if (ambientOscillators.length > 0) return; // Already playing

  const ctx = getAudioContext();
  
  // Resume context if suspended (browser policy)
  if (ctx.state === 'suspended') {
    ctx.resume();
  }

  ambientGain = ctx.createGain();
  ambientGain.gain.value = 0.2; // Increased volume for background
  ambientGain.connect(ctx.destination);

  // Create a soothing chord (Cmaj7 with added 9th: C, E, G, B, D)
  // Frequencies: C3, E3, G3, B3, D4
  const frequencies = [130.81, 164.81, 196.00, 246.94, 293.66]; 
  
  frequencies.forEach((freq, index) => {
    const osc = ctx.createOscillator();
    osc.type = 'sine';
    osc.frequency.value = freq;
    
    // Slight detune for richness
    osc.detune.value = (Math.random() * 10) - 5;

    // Individual gain for each oscillator to create movement
    const oscGain = ctx.createGain();
    oscGain.gain.value = 0.5;
    
    // LFO for volume modulation (tremolo effect)
    const lfo = ctx.createOscillator();
    lfo.type = 'sine';
    lfo.frequency.value = 0.1 + (Math.random() * 0.2); // Slow modulation
    const lfoGain = ctx.createGain();
    lfoGain.gain.value = 0.3; // Modulation depth
    
    lfo.connect(lfoGain);
    lfoGain.connect(oscGain.gain);
    lfo.start();

    osc.connect(oscGain);
    oscGain.connect(ambientGain);
    osc.start();
    
    ambientOscillators.push({ osc, lfo, oscGain, lfoGain });
  });
};

export const stopAmbientMusic = () => {
  ambientOscillators.forEach(node => {
    try {
        node.osc.stop();
        node.lfo.stop();
        node.osc.disconnect();
        node.lfo.disconnect();
    } catch (e) {}
  });
  ambientOscillators = [];
  if (ambientGain) {
      // Fade out
      const ctx = getAudioContext();
      ambientGain.gain.setTargetAtTime(0, ctx.currentTime, 0.5);
      setTimeout(() => {
        try {
            ambientGain.disconnect();
        } catch(e) {}
        ambientGain = null;
      }, 1000);
  }
};
